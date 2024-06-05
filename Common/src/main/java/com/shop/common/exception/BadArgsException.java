package com.shop.common.exception;

/**
 * 参数错误异常
 *
 * @author SK
 * @date 2024/06/05
 */
public class BadArgsException extends BaseException {

    public BadArgsException() {
    }

    public BadArgsException(String msg) {
        super(msg);
    }

}
