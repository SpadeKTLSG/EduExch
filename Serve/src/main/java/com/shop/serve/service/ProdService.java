package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.ProdGreatDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Prod;
import com.shop.pojo.entity.ProdFunc;
import com.shop.pojo.vo.ProdGreatVO;

import java.time.LocalDateTime;
import java.util.List;

public interface ProdService extends IService<Prod> {

    void update4User(ProdGreatDTO prodGreatDTO) throws InstantiationException, IllegalAccessException;

    void update4UserCache(ProdGreatDTO prodGreatDTO) throws InstantiationException, IllegalAccessException;

    void check(ProdLocateDTO prodLocateDTO);

    void freeze(ProdLocateDTO prodLocateDTO);

    Page<ProdGreatDTO> page2Check(Integer current);

    void deleteByNameUser(ProdLocateDTO prodLocateDTO);

    Prod getByNameUser(ProdLocateDTO prodLocateDTO);

    Page<ProdGreatDTO> pageProd(Integer current);

    void publishGood(ProdGreatDTO prodGreatDTO);

    void deleteGood(String name);

    void updateStatus(ProdLocateDTO prodLocateDTO, Integer func);

    ProdGreatVO GetByNameSingle(ProdLocateDTO prodLocateDTO);

    ProdGreatVO GetByNameSingleCache(ProdLocateDTO prodLocateDTO);

    Page<Prod> getPageByCate(String cate, Integer current);

    Page<Prod> pageCateAllProd(String cate, Integer current);

    List<ProdFunc> getOutdateOnes(LocalDateTime time);

    void cooldownUpshowProd(ProdFunc prodFunc);

    void cooldownRotationProd(ProdFunc prodFunc);

    List<ProdFunc> extractList4HotProd();

    void add2HotSearch(ProdFunc prodFunc);


}
