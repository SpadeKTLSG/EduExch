package com.shop.common.exception;

/**
 * 账号不存在异常
 *
 * @author SK
 * @date 2024/06/05
 */
public class AccountNotFoundException extends BaseException {

    public AccountNotFoundException() {
    }

    public AccountNotFoundException(String msg) {
        super(msg);
    }

}
