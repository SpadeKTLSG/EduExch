package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.ProdCateAllDTO;
import com.shop.pojo.entity.ProdCate;

public interface ProdCateService extends IService<ProdCate> {

    void saveCate(ProdCateAllDTO prodCateAllDTO);

    void deleteCate(ProdCateAllDTO prodCateAllDTO);
}
