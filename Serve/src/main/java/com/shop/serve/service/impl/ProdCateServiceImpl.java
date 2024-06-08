package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.exception.SthHasCreatedException;
import com.shop.pojo.dto.ProdCateDTO;
import com.shop.pojo.entity.ProdCate;
import com.shop.serve.mapper.ProdCateMapper;
import com.shop.serve.service.ProdCateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.shop.common.constant.MessageConstant.OBJECT_HAS_ALIVE;

@Slf4j
@Service
public class ProdCateServiceImpl extends ServiceImpl<ProdCateMapper, ProdCate> implements ProdCateService {

    @Override
    public void saveCate(ProdCateDTO prodCateDTO) {

        if (this.getOne(new LambdaQueryWrapper<ProdCate>().eq(ProdCate::getName, prodCateDTO.getName()), false) != null) throw new SthHasCreatedException(OBJECT_HAS_ALIVE);

        this.save(ProdCate.builder()
                .name(prodCateDTO.getName())
                .description(prodCateDTO.getDescription())
                .build());
    }

}
