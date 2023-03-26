package com.boot.learningspirit.common.exception;

import com.boot.learningspirit.common.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.RuntimeErrorException;

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


    /**
     * @param e:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 运行时异常
     * @Date: 2023/2/12 21:10
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeErrorException.class)
    public Result handler(RuntimeException e) {

        return Result.error(40008, e.getMessage());
    }

    /**
     * @param e:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 实体的异常
     * @Date: 2023/2/12 21:10
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return Result.error(4007, objectError.getDefaultMessage());
    }

    /**
     * @param e:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: Assert 断言的异常
     * @Date: 2023/2/12 21:11
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) {

        return Result.error(e.getMessage());
    }

}
