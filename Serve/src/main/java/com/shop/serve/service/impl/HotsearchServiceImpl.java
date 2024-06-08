package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.pojo.dto.HotsearchDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Hotsearch;
import com.shop.pojo.entity.Prod;
import com.shop.pojo.entity.ProdFunc;
import com.shop.serve.mapper.HotsearchMapper;
import com.shop.serve.service.HotsearchService;
import com.shop.serve.service.ProdFuncService;
import com.shop.serve.service.ProdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class HotsearchServiceImpl extends ServiceImpl<HotsearchMapper, Hotsearch> implements HotsearchService {

    @Autowired
    private ProdService prodService;
    @Autowired
    private ProdFuncService prodFuncService;

    @Override
    public void add2Hotsearch(HotsearchDTO hotsearchDTO) {
        Hotsearch hotsearch = new Hotsearch();
        BeanUtils.copyProperties(hotsearchDTO, hotsearch);
        this.save(hotsearch);
    }


    @Override
    public void add2Hotsearch(ProdLocateDTO prodLocateDTO) {
        Prod prod2Get = prodService.getOne(new LambdaQueryWrapper<Prod>()
                .eq(Prod::getName, prodLocateDTO.getName())
                .eq(Prod::getUserId, prodLocateDTO.getUserId()));
        ProdFunc prodFunc = prodFuncService.getById(prod2Get.getId());

        HotsearchDTO hotsearchDTO = HotsearchDTO.builder()
                .prodId(prod2Get.getId())
                .name(prod2Get.getName())
                .visit(prodFunc.getVisit())
                .build();
        this.add2Hotsearch(hotsearchDTO);
    }


    @Override
    public void remove4Hotsearch(HotsearchDTO hotsearchDTO) {

        Hotsearch hotsearch = this.getOne(new LambdaQueryWrapper<Hotsearch>()
                .eq(Hotsearch::getName, hotsearchDTO.getName())
                .eq(Hotsearch::getProdId, hotsearchDTO.getProdId()));
        this.removeById(hotsearch);
    }


    @Override
    public void remove4Hotsearch(ProdLocateDTO prodLocateDTO) {
        Prod prod2Get = prodService.getOne(new LambdaQueryWrapper<Prod>()
                .eq(Prod::getName, prodLocateDTO.getName())
                .eq(Prod::getUserId, prodLocateDTO.getUserId()));
        ProdFunc prodFunc = prodFuncService.getById(prod2Get.getId());

        HotsearchDTO hotsearchDTO = HotsearchDTO.builder()
                .prodId(prod2Get.getId())
                .name(prod2Get.getName())
                .visit(prodFunc.getVisit())
                .build();
        this.add2Hotsearch(hotsearchDTO);
    }


    @Override
    public void clearAllHotsearch() {
        this.remove(null);
        log.debug("清空热搜成功");
    }
}
