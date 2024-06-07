package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.pojo.entity.Upshow;
import com.shop.serve.mapper.UpshowMapper;
import com.shop.serve.service.UpshowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UpshowServiceImpl extends ServiceImpl<UpshowMapper, Upshow> implements UpshowService {
}
