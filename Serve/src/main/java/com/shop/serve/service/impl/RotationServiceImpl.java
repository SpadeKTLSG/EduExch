package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.pojo.entity.Rotation;
import com.shop.serve.mapper.RotationMapper;
import com.shop.serve.service.RotationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class RotationServiceImpl extends ServiceImpl<RotationMapper, Rotation> implements RotationService {
}