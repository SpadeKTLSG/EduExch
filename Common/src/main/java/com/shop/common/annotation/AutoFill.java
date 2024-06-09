package com.shop.common.annotation;

import com.shop.common.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动填充注解
 *
 * @author SK
 * @date 2024/06/09
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {


    /**
     * 数据库操作类型 -> UPDATE INSERT
     */
    OperationType value();
}
