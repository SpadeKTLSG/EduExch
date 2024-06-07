package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.ProdGreatDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Prod;
import com.shop.pojo.vo.ProdGreatVO;

public interface ProdService extends IService<Prod> {

    void update4User(ProdGreatDTO prodGreatDTO) throws InstantiationException, IllegalAccessException;

    void check(ProdLocateDTO prodLocateDTO);

    Page<ProdGreatDTO> page2Check(Integer current);

    void deleteByNameUser(ProdLocateDTO prodLocateDTO);

    Prod getByNameUser(ProdLocateDTO prodLocateDTO);

    Page<ProdGreatDTO> pageProd(Integer current);

    void publishGood(ProdGreatDTO prodGreatDTO);

    void deleteGood(String name);

    void updateStatus(ProdLocateDTO prodLocateDTO, Integer func);

    ProdGreatVO GetByNameSingle(ProdLocateDTO prodLocateDTO);

    Page<Prod> getPageByCate(String cate, Integer current);

    Page<Prod> pageCateAllProd(String cate, Integer current);
}
