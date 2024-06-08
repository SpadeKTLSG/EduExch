package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.dto.UpshowDTO;
import com.shop.pojo.entity.Upshow;


public interface UpshowService extends IService<Upshow> {

    void add2Upshow(UpshowDTO upshowDTO);

    void add2Upshow(ProdLocateDTO prodLocateDTO);

    void remove4Upshow(UpshowDTO upshowDTO);

    void remove4Upshow(ProdLocateDTO prodLocateDTO);


}
