package com.movie.exception;

import com.movie.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice  // 声明此类为全局异常处理类，可以捕获整个应用中所有 Controller 层抛出的指定异常。
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 设置 HTTP 响应状态码为 400（Bad Request），表示客户端请求参数有误
    @ExceptionHandler(MethodArgumentNotValidException.class)  // 捕获 MethodArgumentNotValidException 异常
    public Result handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // 遍历所有校验错误
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            if (fieldName.endsWith("Valid")) {
                fieldName = fieldName.substring(0, fieldName.length() - 5);
            }
            String message = error.getDefaultMessage();

            errors.put(fieldName, message);
        });
        return Result.error(errors);
    }
}
