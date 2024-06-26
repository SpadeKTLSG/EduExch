package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.exception.SthHasCreatedException;
import com.shop.common.exception.SthNotFoundException;
import com.shop.pojo.dto.ProdCateAllDTO;
import com.shop.pojo.entity.ProdCate;
import com.shop.serve.mapper.ProdCateMapper;
import com.shop.serve.service.ProdCateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static com.shop.common.constant.MessageConstant.OBJECT_HAS_ALIVE;
import static com.shop.common.constant.MessageConstant.OBJECT_NOT_ALIVE;

@Slf4j
@Service
public class ProdCateServiceImpl extends ServiceImpl<ProdCateMapper, ProdCate> implements ProdCateService {

    @Override
    public void postCateA(ProdCateAllDTO prodCateAllDTO) {

        if (this.getOne(new LambdaQueryWrapper<ProdCate>()
                .eq(ProdCate::getName, prodCateAllDTO.getName()), false) != null) throw new SthHasCreatedException(OBJECT_HAS_ALIVE);

        this.save(ProdCate.builder()
                .name(prodCateAllDTO.getName())
                .description(prodCateAllDTO.getDescription())
                .build());
    }

    @Override
    public void deleteCateA(ProdCateAllDTO prodCateAllDTO) {

        this.remove(new LambdaQueryWrapper<ProdCate>()
                .eq(ProdCate::getName, prodCateAllDTO.getName()));

    }


    @Override
    public void putCateA(ProdCateAllDTO prodCateAllDTO) {
        ProdCate prodCate = this.getOne(new LambdaQueryWrapper<ProdCate>()
                .eq(ProdCate::getName, prodCateAllDTO.getName()), false);
        if (prodCate == null) throw new SthNotFoundException(OBJECT_NOT_ALIVE);

        BeanUtils.copyProperties(prodCateAllDTO, prodCate);
        this.updateById(prodCate);
    }


}
