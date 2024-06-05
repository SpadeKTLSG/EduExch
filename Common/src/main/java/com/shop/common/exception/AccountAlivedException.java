package com.shop.common.exception;

/**
 * 账号已存在异常
 *
 * @author SK
 * @date 2024/06/05
 */
public class AccountAlivedException extends BaseException {

    public AccountAlivedException() {
    }

    public AccountAlivedException(String msg) {
        super(msg);
    }

}
