package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.ProdCateAllDTO;
import com.shop.pojo.entity.ProdCate;

public interface ProdCateService extends IService<ProdCate> {
    // 查阅主表 ProdService

    /**
     * 保存分类
     */
    void postCateA(ProdCateAllDTO prodCateAllDTO);

    /**
     * 删除分类
     */
    void deleteCateA(ProdCateAllDTO prodCateAllDTO);

    /**
     * 更新分类
     */
    void putCateA(ProdCateAllDTO prodCateAllDTO);
}
