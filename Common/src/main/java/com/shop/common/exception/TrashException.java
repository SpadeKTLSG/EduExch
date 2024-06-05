package com.shop.common.exception;

/**
 * 垃圾数据异常(已使用或过期)
 *
 * @author SK
 * @date 2024/06/05
 */
public class TrashException extends BaseException {

    public TrashException() {
    }

    public TrashException(String msg) {
        super(msg);
    }

}
