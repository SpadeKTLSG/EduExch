package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.HotsearchAllDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Hotsearch;


public interface HotsearchService extends IService<Hotsearch> {

    void add2Hotsearch(HotsearchAllDTO hotsearchAllDTO);

    void add2Hotsearch(ProdLocateDTO prodLocateDTO);

    void remove4Hotsearch(HotsearchAllDTO hotsearchAllDTO);

    void remove4Hotsearch(ProdLocateDTO prodLocateDTO);

    void clearAllHotsearch();
}
