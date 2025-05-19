package com.movie.common;

import lombok.Data;

@Data
public class Result {
    private int code;    // 状态码
    private String msg;   // 提示信息
    private Object data; // 返回数据

    // 快速成功（不带数据）
    public static Result success() {
        Result result = new Result();
        result.code = 200;
        result.msg = "success";
        return result;
    }

    // 成功并携带数据
    public static Result success(Object data) {
        Result result = success();
        result.data = data;
        return result;
    }

    // 快速错误（默认400状态码）
    public static Result error(Object data) {
        Result result = new Result();
        result.code = 400;
        result.msg = "error";
        result.data = data;
        return result;
    }

    // 自定义错误码
    public static Result error(int code, Object data) {
        Result result = new Result();
        result.code = code;
        result.msg = "error";
        result.data = data;
        return result;
    }
}