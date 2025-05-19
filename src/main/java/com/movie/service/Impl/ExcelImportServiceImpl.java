package com.movie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.movie.entity.*;
import com.movie.exception.ExcelParseException;
import com.movie.mapper.CreatorMapper;
import com.movie.mapper.GenreMapper;
import com.movie.mapper.MovieGenreMapper;
import com.movie.mapper.MovieMapper;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ExcelImportServiceImpl implements ExcelImportService {
    private final MovieMapper movieMapper;
    private final GenreMapper genreMapper;
    private final MovieGenreMapper movieGenreMapper;
    private final CreatorMapper creatorMapper;

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

    @Transactional(rollbackFor = Exception.class)
    public void importMovies(MultipartFile file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // 用于批量操作的集合
            List<MovieGenreRelation> movieGenres = new ArrayList<>();
            List<Creator> movieCreators = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // 跳过标题行

                // 1. 处理电影主表
                Movie movie = parseMovie(row);
                Movie existing = movieMapper.selectOne(
                        new LambdaQueryWrapper<Movie>()
                                .eq(Movie::getMovieNo, movie.getMovieNo())
                );

                if (existing != null) {
                    continue; // 跳过已有记录
                }
                movieMapper.insert(movie);

                // 2. 处理电影类型（多对多）
                processGenres(row, movie.getId(), movieGenres);

                // 3. 处理主创人员（导演+演员）
//                processCreators(row, movie.getId(), movieCreators);
            }

            // 批量写入关联表
            if (!movieGenres.isEmpty()) movieGenreMapper.batchInsertRelations(movieGenres);
//            if (!movieCreators.isEmpty()) movieMapper.batchInsertMovieCreators(movieCreators);
        }
    }

    private Movie parseMovie(Row row) throws ExcelParseException{
        Movie movie = new Movie();
        try{
            movie.setMovieNo(getCellStringValue(row, 0)); //电影ID
            movie.setTitle(getCellStringValue(row, 1)); // 标题
            movie.setDescription(getCellStringValue(row, 2)); // 简介
            movie.setCoverUrl(getCellStringValue(row, 3)); // 封面URL
            movie.setReleaseDate(parseLocalDateTime(getCellStringValue(row, 4))); // 上映日期
            movie.setRegion(getCellStringValue(row, 5)); // 地区
            //FIXME  movie.setIsVip(getCellNumericValue(row,));      是否是VIP
            //FIXME  movie.setPlayCount(getCellNumericValue(row,));  播放次数
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

    //FIXME creator表
//    private void processCreators(Row row, Long movieId, List<Creator> movieCreators) {
//        String[] creatorIds = getCellStringValue(row, 12).split("\\|");
//        String[] names = getCellStringValue(row, 13).split("\\|");
//        String roleType = getCellStringValue(row, 11); // 角色类型（导演/演员）
//
//        for (int i = 0; i < names.length; i++) {
//            String name = names[i].trim();
//            Long creatorId = Long.parseLong(creatorIds[i].trim());
//
//            // 主创不存在时插入
//            if (creatorMapper.findIdByName(name) == null) {
//                Creator creator = new Creator();
//                creator.setId(creatorId);
//                creator.setName(name);
//                creatorMapper.insert(creator);
//            }
//
//            // 获取角色ID（1-导演，2-演员）
//            Integer roleId = "导演".equals(roleType) ? 1 : 2;
//            movieCreators.add(new MovieCreator(movieId, creatorId, roleId));
//        }
//    }

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
    private LocalDateTime parseLocalDateTime(String dateStr) {
        try {
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}