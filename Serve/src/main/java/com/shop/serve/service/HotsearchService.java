package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.HotsearchAllDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Hotsearch;


public interface HotsearchService extends IService<Hotsearch> {

    /**
     * 添加一条热搜
     */
    void add2Hotsearch(HotsearchAllDTO hotsearchAllDTO);

    /**
     * 用商品添加一条热搜
     */
    void add2Hotsearch(ProdLocateDTO prodLocateDTO);

    /**
     * 删除一条热搜
     */
    void remove4Hotsearch(HotsearchAllDTO hotsearchAllDTO);

    /**
     * 用商品删除一条热搜
     */
    void remove4Hotsearch(ProdLocateDTO prodLocateDTO);

    /**
     * 清空所有热搜
     */
    void clearAllHotsearch();
}
