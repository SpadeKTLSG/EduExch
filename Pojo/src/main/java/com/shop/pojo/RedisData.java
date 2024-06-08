package com.shop.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Redis数据封装对象
 *
 * @author SK
 * @date 2024/06/08
 */
@Data
public class RedisData<T> {

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 通用数据对象
     */
    private T data;
}
