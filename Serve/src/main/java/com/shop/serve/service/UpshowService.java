package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.dto.UpshowAllDTO;
import com.shop.pojo.entity.Upshow;


public interface UpshowService extends IService<Upshow> {

    void add2Upshow(UpshowAllDTO upshowAllDTO);

    void add2Upshow(ProdLocateDTO prodLocateDTO);

    void remove4Upshow(UpshowAllDTO upshowAllDTO);

    void remove4Upshow(ProdLocateDTO prodLocateDTO);


}
