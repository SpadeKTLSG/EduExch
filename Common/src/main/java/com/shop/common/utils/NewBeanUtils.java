package com.shop.common.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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

    /**
     * 获取所有的属性值为空属性名数组Plus, 用于多个目标类
     */
    public static String[] getNullPropertyNamesPlus(Object source, Class<?>... targetClasses) {
        final List<String> sourceFields = Arrays.stream(source.getClass().getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());

        final List<String> targetFields = Arrays.stream(targetClasses)
                .flatMap(c -> Arrays.stream(c.getDeclaredFields()))
                .map(Field::getName)
                .collect(Collectors.toList());

        // Create a new list that is a copy of sourceFields
        List<String> sourceFieldsCopy = new ArrayList<>(sourceFields);

        // Remove all elements from sourceFieldsCopy that are also present in targetFields
        sourceFieldsCopy.removeAll(targetFields);

        return sourceFields.toArray(new String[0]);
    }
}
