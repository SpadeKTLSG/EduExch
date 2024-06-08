package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.HotsearchDTO;
import com.shop.pojo.entity.Hotsearch;


public interface HotsearchService extends IService<Hotsearch> {

    void add2HotSearch(HotsearchDTO hotsearchDTO);

    void remove4HotSearch(HotsearchDTO hotsearchDTO);

    void clearAllHotSearch();
}
