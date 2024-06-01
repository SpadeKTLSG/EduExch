package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.pojo.entity.UserFunc;
import com.shop.serve.mapper.UserFuncMapper;
import com.shop.serve.service.UserFuncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserFuncServiceImpl extends ServiceImpl<UserFuncMapper, UserFunc> implements UserFuncService {
}
