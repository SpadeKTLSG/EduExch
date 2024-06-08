package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.pojo.dto.RotationDTO;
import com.shop.pojo.entity.Rotation;
import com.shop.serve.mapper.RotationMapper;
import com.shop.serve.service.RotationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class RotationServiceImpl extends ServiceImpl<RotationMapper, Rotation> implements RotationService {

    @Override
    public void add2Rotation(RotationDTO rotationDTO) {
        Rotation rotation = new Rotation();
        BeanUtils.copyProperties(rotationDTO, rotation);
        this.save(rotation);

    }

    @Override
    public void remove4Rotation(RotationDTO rotationDTO) {
        Rotation rotation = this.getOne(new LambdaQueryWrapper<Rotation>()
                .eq(Rotation::getProdId, rotationDTO.getProdId()));
        this.removeById(rotation);
    }
}
