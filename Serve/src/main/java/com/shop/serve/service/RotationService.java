package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.RotationDTO;
import com.shop.pojo.entity.Rotation;


public interface RotationService extends IService<Rotation> {

    void add2Rotation(RotationDTO rotationDTO);

    void remove4Rotation(RotationDTO rotationDTO);
}
