package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.dto.RotationAllDTO;
import com.shop.pojo.entity.Rotation;


public interface RotationService extends IService<Rotation> {

    void add2Rotation(RotationAllDTO rotationAllDTO);

    void add2Rotation(ProdLocateDTO prodLocateDTO);

    void remove4Rotation(RotationAllDTO rotationAllDTO);

    void remove4Rotation(ProdLocateDTO prodLocateDTO);
}
