package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.dto.RotationAllDTO;
import com.shop.pojo.entity.Rotation;


public interface RotationService extends IService<Rotation> {

    /**
     * 添加到Rotation
     */
    void add2Rotation(RotationAllDTO rotationAllDTO);

    /**
     * 添加到Rotation
     */
    void add2Rotation(ProdLocateDTO prodLocateDTO);

    /**
     * 从Rotation中移除
     */
    void remove4Rotation(RotationAllDTO rotationAllDTO);

    /**
     * 从Rotation中移除
     */
    void remove4Rotation(ProdLocateDTO prodLocateDTO);
}
