package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.OrderAllDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Order;
import com.shop.pojo.vo.OrderGreatVO;

public interface OrderService extends IService<Order> {
    OrderGreatVO showOne(OrderAllDTO orderAllDTO);

    void openOrder(ProdLocateDTO prodLocateDTO);

    void closeOrder(OrderAllDTO orderAllDTO);

    void sellerCheck(OrderAllDTO orderAllDTO);

    void buyerCheck(OrderAllDTO orderAllDTO);

    void allCheck(OrderAllDTO orderAllDTO);

    OrderGreatVO getOrderDetails(OrderAllDTO orderAllDTO);
}
