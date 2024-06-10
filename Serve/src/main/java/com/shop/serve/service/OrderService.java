package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.OrderAllDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Order;
import com.shop.pojo.vo.OrderGreatVO;

public interface OrderService extends IService<Order> {

    /**
     * 查看一个订单详情
     */
    OrderGreatVO orderDetail(OrderAllDTO orderAllDTO);

    /**
     * 开启交易
     */
    void startOrder(ProdLocateDTO prodLocateDTO);

    /**
     * 开启秒杀交易
     */
    void seckillStartOrder(ProdLocateDTO prodLocateDTO);

    /**
     * 关闭交易
     */
    void closeOrder(OrderAllDTO orderAllDTO);

    /**
     * 卖家确认回复[1]
     */
    void sellerKnowAnswer(OrderAllDTO orderAllDTO);

    /**
     * 买家确认回复[2]
     */
    void buyerKnowAnswer(OrderAllDTO orderAllDTO);

    /**
     * 卖家确认关闭[3]
     */
    void sellerKnowClose(OrderAllDTO orderAllDTO);

    /**
     * 完整订单详情
     */
    OrderGreatVO orderDetails(OrderAllDTO orderAllDTO);


}
