package com.movie.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ExcelImportService {
    void importMovies(MultipartFile file) throws IOException;
}
