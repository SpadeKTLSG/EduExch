package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.dto.UpshowAllDTO;
import com.shop.pojo.entity.Upshow;


public interface UpshowService extends IService<Upshow> {

    /**
     * 添加到Upshow
     */
    void add2Upshow(UpshowAllDTO upshowAllDTO);

    /**
     * 添加到Upshow
     */
    void add2Upshow(ProdLocateDTO prodLocateDTO);

    /**
     * 从Upshow中移除
     */
    void remove4Upshow(UpshowAllDTO upshowAllDTO);

    /**
     * 从Upshow中移除
     */
    void remove4Upshow(ProdLocateDTO prodLocateDTO);


}
