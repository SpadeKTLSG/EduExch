package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.OrderAllDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Order;
import com.shop.pojo.vo.OrderGreatVO;

public interface OrderService extends IService<Order> {

    OrderGreatVO orderDetail(OrderAllDTO orderAllDTO);

    void startOrder(ProdLocateDTO prodLocateDTO);

    void closeOrder(OrderAllDTO orderAllDTO);

    void sellerKnowAnswer(OrderAllDTO orderAllDTO);

    void buyerKnowAnswer(OrderAllDTO orderAllDTO);

    void sellerKnowClose(OrderAllDTO orderAllDTO);

    OrderGreatVO orderDetails(OrderAllDTO orderAllDTO);
}
