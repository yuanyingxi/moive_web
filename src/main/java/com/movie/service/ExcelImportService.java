package com.movie.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ExcelImportService {
    //处理genre,movie,movie_genre,creator,movie_creator
    void importMovies(MultipartFile file) throws IOException;
}
