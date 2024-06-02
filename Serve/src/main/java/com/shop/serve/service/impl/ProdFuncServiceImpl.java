package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.pojo.entity.ProdFunc;
import com.shop.serve.mapper.ProdFuncMapper;
import com.shop.serve.service.ProdFuncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProdFuncServiceImpl extends ServiceImpl<ProdFuncMapper, ProdFunc> implements ProdFuncService {
}
