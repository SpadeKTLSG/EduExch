package com.shop.common.utils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.entity.User;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 自制BeanUtils工具类
 *
 * @author SK
 * @date 2024/06/01
 */
public class NewBeanUtils {

    /**
     * 获取所有的属性值为空属性名数组
     */
    public static String[] getNullPropertyNames(Object source) {
        // use basic Java
       /*

       final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        return Stream.of(pds).map(pd -> {
            Object srcValue = src.getPropertyValue(pd.getName());
            return srcValue == null ? pd.getName() : "";
        }).filter(name -> !name.isEmpty()).toArray(String[]::new);
*/
        //now use Apache Commons BeanUtils

        PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(source);

        return Stream.of(pds)
                .filter(pd -> {
                    try {
                        return pd.getReadMethod().invoke(source) == null;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(PropertyDescriptor::getName)
                .toArray(String[]::new);
    }

    /**
     * DTO映射服务
     */
    public static <T> void dtoMapService(Map<Object, IService> dtoServiceMap, Long id, Optional<T> optionalProd) {
        for (Map.Entry<Object, IService> entry : dtoServiceMap.entrySet()) {
            Object dto = entry.getKey();
            IService service = entry.getValue();
            String[] nullPN = getNullPropertyNames(dto);// 判断nullPN
            Object target = service.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, id));
            BeanUtils.copyProperties(dto, target, nullPN);
            service.updateById(target);
        }
    }
}
