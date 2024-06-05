package com.shop.common.exception;

/**
 * 东西丢了异常
 *
 * @author SK
 * @date 2024/06/05
 */
public class SthNotFoundException extends BaseException {

    public SthNotFoundException() {
    }

    public SthNotFoundException(String msg) {
        super(msg);
    }

}
