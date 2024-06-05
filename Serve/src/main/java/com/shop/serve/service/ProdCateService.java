package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.ProdCateDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.ProdCate;

public interface ProdCateService extends IService<ProdCate> {

    void saveCate(ProdCateDTO prodCateDTO);

    void deleteByNameUser(ProdLocateDTO prodLocateDTO);
}
