package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.ProdGreatDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Prod;

public interface ProdService extends IService<Prod> {

    void update4User(ProdGreatDTO prodGreatDTO) throws InstantiationException, IllegalAccessException;

    void check(ProdLocateDTO prodLocateDTO);

    Page<ProdGreatDTO> page2Check(Integer current);

}
