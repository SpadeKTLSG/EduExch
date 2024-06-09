package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.dto.UpshowAllDTO;
import com.shop.pojo.entity.Prod;
import com.shop.pojo.entity.Upshow;
import com.shop.serve.mapper.UpshowMapper;
import com.shop.serve.service.ProdFuncService;
import com.shop.serve.service.ProdService;
import com.shop.serve.service.UpshowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UpshowServiceImpl extends ServiceImpl<UpshowMapper, Upshow> implements UpshowService {

    @Autowired
    private ProdService prodService;
    @Autowired
    private ProdFuncService prodFuncService;


    @Override
    public void add2Upshow(UpshowAllDTO upshowAllDTO) {
        Upshow upshow = new Upshow();
        BeanUtils.copyProperties(upshowAllDTO, upshow);
        this.save(upshow);
    }


    @Override
    public void add2Upshow(ProdLocateDTO prodLocateDTO) {

        Prod prod2Get = prodService.getOne(new LambdaQueryWrapper<Prod>()
                .eq(Prod::getName, prodLocateDTO.getName())
                .eq(Prod::getUserId, prodLocateDTO.getUserId()));

        UpshowAllDTO rotationDTO = UpshowAllDTO.builder()
                .prodId(prod2Get.getId())
                .name(prod2Get.getName())
                .build();

        this.add2Upshow(rotationDTO);
    }


    @Override
    public void remove4Upshow(UpshowAllDTO upshowAllDTO) {

        Upshow upshow = this.getOne(new LambdaQueryWrapper<Upshow>()
                .eq(Upshow::getProdId, upshowAllDTO.getProdId()));

        this.removeById(upshow);
    }


    @Override
    public void remove4Upshow(ProdLocateDTO prodLocateDTO) {

        Prod prod2Get = prodService.getOne(new LambdaQueryWrapper<Prod>()
                .eq(Prod::getName, prodLocateDTO.getName())
                .eq(Prod::getUserId, prodLocateDTO.getUserId()));

        UpshowAllDTO rotationDTO = UpshowAllDTO.builder()
                .prodId(prod2Get.getId())
                .name(prod2Get.getName())
                .build();

        this.remove4Upshow(rotationDTO);
    }


}
