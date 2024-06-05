package com.shop.common.exception;

/**
 * 密码错误异常
 *
 * @author SK
 * @date 2024/06/05
 */
public class PasswordErrorException extends BaseException {

    public PasswordErrorException() {
    }

    public PasswordErrorException(String msg) {
        super(msg);
    }

}
