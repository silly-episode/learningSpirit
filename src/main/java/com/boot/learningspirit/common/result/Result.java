package com.boot.learningspirit.common.result;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/1/30 10:01
 * @FileName: Result
 * @Description: 响应返回结果
 */
@Data
public class Result<T> {

    private int code;
    private String msg;
    private T data;


    private Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(T data, CodeMsg codeMsg) {
        this.data = data;
        if (codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }

    private Result(CodeMsg codeMsg) {
        if (codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }

    /**
     * @param data: 成功的返回数据
     * @Return: String
     * @Author: DengYinzhe
     * @Description: 成功时调用，需返回数据
     * @Date: 2023/2/6 21:09
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(data, CodeMsg.SUCCESS);
    }

    /**
     * @Return: String
     * @Author: DengYinzhe
     * @Description: 成功时调用，不需返回数据
     * @Date: 2023/2/6 21:10
     */
    public static <T> Result<T> success() {
        return new Result<>(CodeMsg.SUCCESS);
    }

    /**
     * @param code:
     * @param msg:
     * @Return: String
     * @Author: DengYinzhe
     * @Description: 失败时候的调用，自定义code和msg
     * @Date: 2023/2/6 21:11
     */
    public static <T> Result<T> error(Integer code, String msg) {
        return new Result<T>(code, msg);
    }

    /**
     * @param codeMsg:
     * @Return: String
     * @Author: DengYinzhe
     * @Description: 失败时候的调用，使用CodeMsg中固定的code和msg
     * @Date: 2023/2/6 21:12
     */
    public static <T> Result<T> error(CodeMsg codeMsg) {
        return new Result<>(codeMsg);
    }

    /**
     * @param msg:
     * @Return: String
     * @Author: DengYinzhe
     * @Description: 失败时候的调用，http的code和自定义的msg
     * @Date: 2023/2/6 21:12
     */
    public static <T> Result<T> error(String msg) {
        CodeMsg codeMsg = new CodeMsg(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
        return new Result<>(codeMsg);
    }
}
