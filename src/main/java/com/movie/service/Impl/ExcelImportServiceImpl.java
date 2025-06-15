package com.movie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.movie.entity.*;
import com.movie.exception.ExcelParseException;
import com.movie.mapper.*;
import com.movie.service.ExcelImportService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ExcelImportServiceImpl implements ExcelImportService {
    private final MovieMapper movieMapper;
    private final GenreMapper genreMapper;
    private final CreatorMapper creatorMapper;
    private final MovieGenreMapper movieGenreMapper;
    private final MovieCreatorMapper movieCreatorMapper;

    @PostConstruct
    public void initPresetGenres() {
        List<Genre> presetGenres = Arrays.asList(
                new Genre(null, "剧情"),
                new Genre(null, "喜剧"),
                new Genre(null, "动作"),
                new Genre(null, "爱情"),
                new Genre(null, "科幻"),
                new Genre(null, "犯罪"),
                new Genre(null, "冒险"),
                new Genre(null, "奇幻"),
                new Genre(null, "悬疑"),
                new Genre(null, "惊悚")
        );

        presetGenres.forEach(genre -> {
            if (genreMapper.selectIdByName(genre.getName()) == null) {
                genreMapper.insert(genre); // 依赖数据库自增ID
            }
        });
    }

    /**
     * 处理genre,movie,movie_genre,creator,movie_creator
     * @param file excel文件
     * @throws IOException 输入输出错误
     */
    @Transactional(rollbackFor = Exception.class)
    public void importMovies(MultipartFile file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<MovieGenreRelation> movieGenres = new ArrayList<>();
            List<MovieCreator> movieCreators = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                // 处理电影基础信息
                Movie movie = parseMovie(row);
                Movie existing = movieMapper.selectOne(
                        new LambdaQueryWrapper<Movie>()
                                .eq(Movie::getMovieNo, movie.getMovieNo())
                );

                // 无论电影是否存在都处理主创
                if (existing == null) {
                    movieMapper.insert(movie);
                    processGenres(row, movie.getId(), movieGenres);
                } else {
                    movie = existing; // 使用已存在的电影记录
                }

                // 重点：始终处理主创人员
                processCreators(row, movie.getId(), movieCreators);
            }

            // 批量插入关联
            if (!movieGenres.isEmpty()) movieGenreMapper.batchInsertRelations(movieGenres);
            if (!movieCreators.isEmpty()) movieCreatorMapper.batchInsertRelations(movieCreators);
        }
    }


    private Movie parseMovie(Row row) throws ExcelParseException{
        Movie movie = new Movie();
        try{
            movie.setMovieNo(getCellStringValue(row, 0)); //电影ID
            movie.setTitle(getCellStringValue(row, 1)); // 标题
            movie.setDescription(getCellStringValue(row, 2)); // 简介
            movie.setCoverUrl("http://fce48f66.natappfree.cc/api/MovieCover/"+getCellStringValue(row, 0)+".jpg"); // 封面URL
            movie.setReleaseDate(parseLocalDate(getCellStringValue(row, 4))); // 上映日期
            movie.setRegion(getCellStringValue(row, 5)); // 地区
            double rate = getCellNumericValue(row, 7);
            int isVip = 0;
            if(rate >= 9.2){ isVip = 1;}
            movie.setIsVip(isVip);      //  是否是VIP 评分大于等于9是vip
            movie.setUrl(isVip==1?"http://fce48f66.natappfree.cc/AveMujica.mp4":"http://fce48f66.natappfree.cc/MyGo.mp4");
            movie.setPlayCount(2 * (long)getCellNumericValue(row, 8));  //热度设置为评分人数的两倍
            movie.setDuration(parseDurationAndRegion(getCellStringValue(row, 6))); // 时长
            movie.setAverageRating(BigDecimal.valueOf(getCellNumericValue(row, 7))); // 评分
            movie.setRatingCount((long)getCellNumericValue(row, 8)); // 评分人数
        }catch (NumberFormatException e) {
            throw new ExcelParseException("数值格式错误", row.getRowNum() + 1); // 标记错误行
        }
        return movie;
    }

    //FIXME genre表 + movie_genre表
    private void processGenres(Row row, Long movieId,
                               List<MovieGenreRelation> movieGenres) {
        // 获取类型名称列（第9列）
        String[] rawGenres = getCellStringValue(row, 9).split("\\|");

        for (String genreName : rawGenres) {
            // 清洗数据（处理繁体、空格、错别字）
            String cleanedName = genreName.trim();

            // 查找或创建类型
            Integer existing = genreMapper.selectIdByName(cleanedName);
            Integer genreId;

            if (existing != null) {
                genreId = existing;
            } else {
                // 插入新类型（自动生成ID从11开始）
                Genre newGenre = new Genre();
                newGenre.setName(cleanedName);
                genreMapper.insert(newGenre);
                genreId = newGenre.getId();
            }

            // 构建关联关系
            movieGenres.add(new MovieGenreRelation(movieId, genreId));
        }
    }

    private int parseDurationAndRegion(String durationStr) {
        // 正则匹配：提取"分"字前的所有非空字符，并验证是否为纯数字
        Matcher matcher = Pattern.compile("^\\s*(\\d+)\\s*分").matcher(durationStr);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    //FIXME creator movie_creator表
    private void processCreators(Row row, Long movieId, List<MovieCreator> movieCreators) {
        // 从指定列获取数据
        String name = getCellStringValue(row, 13).trim();  // 第13列：姓名
        String photoUrl = getCellStringValue(row, 15);    // 第15列：照片URL
        String roleType = getCellStringValue(row, 11);    // 第11列：角色类型

        if (name.isEmpty()) return;

        // 处理角色类型
        Long roleId = "导演".equalsIgnoreCase(roleType) ? 1L : 2L;

        // 查询或创建主创
        Creator creator = creatorMapper.selectOne(
                new LambdaQueryWrapper<Creator>().eq(Creator::getName, name)
        );

        if (creator == null) {
            creator = new Creator();
            creator.setName(name);
            creator.setPhotoUrl(photoUrl);
            creatorMapper.insert(creator);
        }

        // 直接创建关联（不检查重复）
        movieCreators.add(new MovieCreator(movieId, creator.getId(), roleId));
    }



    // 安全获取字符串值（适配文本型数字）
    private String getCellStringValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue()); // 处理整数型数值
            default -> "";
        };
    }

    // 安全获取数值（适配文本型数字转换）
    private double getCellNumericValue(Row row, int cellNum) {
        try {
            Cell cell = row.getCell(cellNum);
            return cell != null ? cell.getNumericCellValue() : 0;
        } catch (IllegalStateException e) {
            // 处理文本型数字（如 "7.5" 被存为字符串）
            String strValue = getCellStringValue(row, cellNum);
            return NumberUtils.toDouble(strValue, 0);
        }
    }

    // 日期解析（处理Excel日期格式）
    private LocalDateTime parseLocalDate(String input) {
        String dateStr = input.split("\\(")[0].trim();
        if (dateStr.matches("\\d{4}")) {
            dateStr += "-01-01";
        } else if (dateStr.matches("\\d{4}-\\d{2}")) {
            dateStr += "-01";
        } else if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("不支持的日期格式: " + dateStr);
        }
        return LocalDate.parse(dateStr).atStartOfDay();
    }
}