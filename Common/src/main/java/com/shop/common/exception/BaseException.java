package com.shop.common.exception;

/**
 * 业务异常
 *
 * @author SK
 * @date 2024/06/05
 */
public class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

}
