package com.movie.controller;

import com.movie.common.Result;
import com.movie.exception.ExcelParseException;
import com.movie.service.ExcelImportService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelImportController {
    private final ExcelImportService excelImportService;

    @PostMapping("/import")
    public ResponseEntity<Result> importMovies(
            @RequestParam("file") MultipartFile file) {
        try {
            // 校验文件类型（匹配图片中的Excel处理需求）
            if (!FilenameUtils.getExtension(file.getOriginalFilename()).equalsIgnoreCase("xlsx")) {
                return ResponseEntity.badRequest()
                        .body(Result.error(400, "仅支持.xlsx格式文件"));
            }

            excelImportService.importMovies(file);
            return ResponseEntity.ok(Result.success("导入成功"));

        } catch (ExcelParseException e) {
            return ResponseEntity.badRequest()
                    .body(Result.error(400, "第 " + e.getErrorRow() + " 行数据错误: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Result.error(500, "系统错误：" + e.getMessage()));
        }
    }
}