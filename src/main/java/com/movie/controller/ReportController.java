package com.movie.controller;

import com.movie.entity.MovieRankingVO;
import com.movie.helper.JwtHelper;
import com.movie.service.MovieService;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final MovieService movieService;
    private final JwtHelper jwtHelper;
    @Autowired
    public ReportController(MovieService movieService, JwtHelper jwtHelper) {
        this.movieService = movieService;
        this.jwtHelper = jwtHelper;
    }

    @GetMapping("/ranking")
    public void exportRanking(HttpServletResponse response,
                              @RequestHeader("Authorization") String token)
            throws AuthException{

        // JWT校验逻辑
        if (!jwtHelper.validateToken(token)) {  // 确保使用正确实例名称
            throw new AuthException("无效的访问令牌");
        }


        List<MovieRankingVO> rankings = movieService.getPlayRankings();

        // 创建Excel工作簿
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("电影播放榜单");

            // 创建表头
            XSSFRow headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("排名");
            headerRow.createCell(1).setCellValue("电影名称");
            headerRow.createCell(2).setCellValue("播放次数");

            // 填充数据
            int rowNum = 1;
            for (MovieRankingVO ranking : rankings) {
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(ranking.getRank());
                row.createCell(1).setCellValue(ranking.getTitle());
                row.createCell(2).setCellValue(ranking.getPlayCount());
            }

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=movie_ranking.xlsx");

            // 输出流
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("生成报表失败");
        }

    }
}
