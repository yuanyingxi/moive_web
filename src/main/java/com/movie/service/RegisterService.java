package com.movie.service;


import com.movie.DTO.RegisterDTO;
import org.springframework.stereotype.Service;

@Service
public interface RegisterService {

    void register(RegisterDTO registerDTO);

}
