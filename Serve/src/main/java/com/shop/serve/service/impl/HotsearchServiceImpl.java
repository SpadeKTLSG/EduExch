package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.pojo.dto.HotsearchDTO;
import com.shop.pojo.entity.Hotsearch;
import com.shop.serve.mapper.HotsearchMapper;
import com.shop.serve.service.HotsearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class HotsearchServiceImpl extends ServiceImpl<HotsearchMapper, Hotsearch> implements HotsearchService {


    @Override
    public void add2HotSearch(HotsearchDTO hotsearchDTO) {
        Hotsearch hotsearch = new Hotsearch();
        BeanUtils.copyProperties(hotsearchDTO, hotsearch);
        this.save(hotsearch);
    }

    @Override
    public void remove4HotSearch(HotsearchDTO hotsearchDTO) {

        Hotsearch hotsearch = this.getOne(new LambdaQueryWrapper<Hotsearch>()
                .eq(Hotsearch::getName, hotsearchDTO.getName())
                .eq(Hotsearch::getProdId, hotsearchDTO.getProdId()));
        this.removeById(hotsearch);
    }

    @Override
    public void clearAllHotSearch() {
        this.remove(null);
        log.debug("清空热搜成功");
    }
}
