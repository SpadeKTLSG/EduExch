package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.pojo.dto.UpshowDTO;
import com.shop.pojo.entity.Upshow;
import com.shop.serve.mapper.UpshowMapper;
import com.shop.serve.service.UpshowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UpshowServiceImpl extends ServiceImpl<UpshowMapper, Upshow> implements UpshowService {


    @Override
    public void add2Upshow(UpshowDTO upshowDTO) {
        Upshow upshow = new Upshow();
        BeanUtils.copyProperties(upshowDTO, upshow);
        this.save(upshow);
    }

    @Override
    public void remove4Upshow(UpshowDTO upshowDTO) {
        Upshow upshow = this.getOne(new LambdaQueryWrapper<Upshow>()
                .eq(Upshow::getName, upshowDTO.getName())
                .eq(Upshow::getProdId, upshowDTO.getProdId()));

        this.removeById(upshow);
    }


}
