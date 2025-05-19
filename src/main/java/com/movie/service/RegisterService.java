package com.movie.service;


import com.movie.DTO.RegisterDTO;
import org.springframework.stereotype.Service;

@Service
public interface RegisterService {

    Boolean isUserRegistered(RegisterDTO registerDTO);

    void insertUser(RegisterDTO registerDTO);
}
