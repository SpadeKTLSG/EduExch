package com.shop.common.exception;

/**
 * 用户未登录异常
 *
 * @author SK
 * @date 2024/06/04
 */
public class NotLoginException extends BaseException {

    public NotLoginException() {
    }

    public NotLoginException(String msg) {
        super(msg);
    }

}
