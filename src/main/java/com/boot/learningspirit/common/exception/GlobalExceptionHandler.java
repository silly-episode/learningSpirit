package com.boot.learningspirit.common.exception;

import com.boot.learningspirit.common.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/2/20 10:09
 * @FileName: GlobalExceptionHandler
 * @Description:
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    //token失效异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = TokenException.class)
    public Result handler(TokenException e) {
        return Result.error(401, e.getMessage());
    }

}
