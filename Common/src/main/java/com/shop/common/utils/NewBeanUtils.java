package com.shop.common.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.stream.Stream;

/**
 * 新的BeanUtils
 *
 * @author SK
 * @date 2024/06/01
 */
public class NewBeanUtils {

    /**
     * 获取所有的属性值为空属性名数组
     *
     * @param source 源
     * @return {@link String[] }
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        return Stream.of(pds)
                .map(pd -> {
                    Object srcValue = src.getPropertyValue(pd.getName());
                    return srcValue == null ? pd.getName() : "";
                })
                .filter(name -> !name.isEmpty())
                .toArray(String[]::new);
    }
}
