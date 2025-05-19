package com.movie.exception;

import lombok.Getter;

@Getter
public class ExcelParseException extends RuntimeException {
    private final int errorRow;

    public ExcelParseException(String message, int errorRow) {
        super(message);
        this.errorRow = errorRow;
    }

}