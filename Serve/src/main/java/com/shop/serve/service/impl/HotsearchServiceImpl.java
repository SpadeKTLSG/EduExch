package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.pojo.entity.Hotsearch;
import com.shop.serve.mapper.HotsearchMapper;
import com.shop.serve.service.HotsearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class HotsearchServiceImpl extends ServiceImpl<HotsearchMapper, Hotsearch> implements HotsearchService {
}
