package com.shop.common.exception;

/**
 * 禁止操作异常
 *
 * @author SK
 * @date 2024/06/05
 */
public class BlockActionException extends BaseException {

    public BlockActionException() {
    }

    public BlockActionException(String msg) {
        super(msg);
    }

}
