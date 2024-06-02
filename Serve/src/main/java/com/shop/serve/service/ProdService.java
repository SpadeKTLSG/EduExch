package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.ProdGreatDTO;
import com.shop.pojo.entity.Prod;

public interface ProdService extends IService<Prod> {
    void updateUserGreatDTO(ProdGreatDTO prodGreatDTO) throws InstantiationException, IllegalAccessException;
}
