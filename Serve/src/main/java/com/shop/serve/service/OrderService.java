package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.OrderAllDTO;
import com.shop.pojo.entity.Order;
import com.shop.pojo.vo.OrderGreatVO;

public interface OrderService extends IService<Order> {
    OrderGreatVO showOne(OrderAllDTO orderAllDTO);
}
