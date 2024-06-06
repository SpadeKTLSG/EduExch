package com.shop.common.exception;

/**
 * 不合法输入异常
 *
 * @author SK
 * @date 2024/06/05
 */
public class InvalidInputException extends BaseException {

    public InvalidInputException() {
    }

    public InvalidInputException(String msg) {
        super(msg);
    }

}
