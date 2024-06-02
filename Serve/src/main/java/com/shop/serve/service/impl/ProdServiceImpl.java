package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.pojo.dto.ProdAllDTO;
import com.shop.pojo.dto.ProdFuncAllDTO;
import com.shop.pojo.dto.ProdGreatDTO;
import com.shop.pojo.entity.Prod;
import com.shop.serve.mapper.ProdMapper;
import com.shop.serve.service.ProdCateService;
import com.shop.serve.service.ProdFuncService;
import com.shop.serve.service.ProdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.shop.common.utils.NewBeanUtils.dtoMapService;

@Slf4j
@Service
public class ProdServiceImpl extends ServiceImpl<ProdMapper, Prod> implements ProdService {

    @Autowired
    private ProdFuncService prodFuncService;
    @Autowired
    private ProdCateService prodCateService;

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public void updateUserGreatDTO(ProdGreatDTO prodGreatDTO) throws InstantiationException, IllegalAccessException {
        //联表选择性更新字段
        Optional<Prod> optionalProd = Optional.ofNullable(this.getOne(Wrappers.<Prod>lambdaQuery().eq(Prod::getName, prodGreatDTO.getName())));
        if (optionalProd.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }

        //这里商品的userId是自己
//        optionalProd.get().setUserId(UserHolder.getUser().getId());
        // 调试选项
        optionalProd.get().setUserId(1L);


        @SuppressWarnings("rawtypes")
        Map<Object, IService> dtoServiceMap = new HashMap<>();
        dtoServiceMap.put(createDTOFromProdGreatDTO(prodGreatDTO, ProdAllDTO.class), this);
        dtoServiceMap.put(createDTOFromProdGreatDTO(prodGreatDTO, ProdFuncAllDTO.class), prodFuncService);

        dtoMapService(dtoServiceMap, optionalProd.get().getId(), optionalProd);
    }


    /**
     * 从ProdGreatDTO创建DTO
     */
    @SuppressWarnings("deprecation")
    private <T> T createDTOFromProdGreatDTO(ProdGreatDTO prodGreatDTO, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        T dto = clazz.newInstance();
        BeanUtils.copyProperties(prodGreatDTO, dto);
        return dto;
    }
}
