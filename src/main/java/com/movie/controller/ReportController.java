package com.movie.controller;

import com.movie.entity.MovieRankingVO;
import com.movie.service.MovieService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    private final MovieService movieService;

    @GetMapping("/ranking")
    public ResponseEntity<Resource> exportPlayRanking() throws IOException {
        List<MovieRankingVO> rankings = movieService.getPlayRankings();

        // 生成Excel文件
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("电影播放榜单");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("排名");
        headerRow.createCell(1).setCellValue("电影名称");
        headerRow.createCell(2).setCellValue("播放次数");

        // 填充数据
        for (int i = 0; i < rankings.size(); i++) {
            MovieRankingVO item = rankings.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(item.getRank());
            row.createCell(1).setCellValue(item.getTitle());
            row.createCell(2).setCellValue(item.getPlayCount());
        }
        if (rankings.isEmpty()) {
            throw new IllegalArgumentException("无有效数据可导出");
        }
        // 写入临时文件
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        // 设置HTTP响应
        ByteArrayResource resource = new ByteArrayResource(out.toByteArray());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=play_ranking.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body((Resource) resource);
    }
}