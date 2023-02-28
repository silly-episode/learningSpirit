package com.boot.learningspirit.common.exception;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/2/20 10:09
 * @FileName: TokenException
 * @Description:
 */
public class TokenException extends RuntimeException {
    public TokenException() {
        super();
    }

    public TokenException(String msg) {
        super(msg);
    }
}
