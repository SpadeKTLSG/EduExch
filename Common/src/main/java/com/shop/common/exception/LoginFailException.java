package com.shop.common.exception;

/**
 * 登录失败
 *
 * @author SK
 * @date 2024/06/05
 */
public class LoginFailException extends BaseException {

    public LoginFailException() {
        super();
    }

    public LoginFailException(String msg) {
        super(msg);
    }

}
