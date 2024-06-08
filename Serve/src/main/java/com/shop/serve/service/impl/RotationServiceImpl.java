package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.dto.RotationDTO;
import com.shop.pojo.entity.Prod;
import com.shop.pojo.entity.Rotation;
import com.shop.serve.mapper.RotationMapper;
import com.shop.serve.service.ProdService;
import com.shop.serve.service.RotationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class RotationServiceImpl extends ServiceImpl<RotationMapper, Rotation> implements RotationService {

    @Autowired
    private ProdService prodService;



    @Override
    public void add2Rotation(RotationDTO rotationDTO) {
        Rotation rotation = new Rotation();
        BeanUtils.copyProperties(rotationDTO, rotation);
        this.save(rotation);

    }

    @Override
    public void add2Rotation(ProdLocateDTO prodLocateDTO) {
        Prod prod2Get = prodService.getOne(new LambdaQueryWrapper<Prod>()
                .eq(Prod::getName, prodLocateDTO.getName())
                .eq(Prod::getUserId, prodLocateDTO.getUserId()));
        RotationDTO rotationDTO = RotationDTO.builder()
                .prodId(prod2Get.getId())
                .name(prod2Get.getName())
                .build();
        this.add2Rotation(rotationDTO);
    }

    @Override
    public void remove4Rotation(RotationDTO rotationDTO) {
        Rotation rotation = this.getOne(new LambdaQueryWrapper<Rotation>()
                .eq(Rotation::getProdId, rotationDTO.getProdId()));
        this.removeById(rotation);
    }

    @Override
    public void remove4Rotation(ProdLocateDTO prodLocateDTO) {
        Prod prod2Get = prodService.getOne(new LambdaQueryWrapper<Prod>()
                .eq(Prod::getName, prodLocateDTO.getName())
                .eq(Prod::getUserId, prodLocateDTO.getUserId()));
        RotationDTO rotationDTO = RotationDTO.builder()
                .prodId(prod2Get.getId())
                .name(prod2Get.getName())
                .build();
        this.remove4Rotation(rotationDTO);
    }
}
