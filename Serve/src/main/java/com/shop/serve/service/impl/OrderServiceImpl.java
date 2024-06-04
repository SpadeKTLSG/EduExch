package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.exception.BadArgsException;
import com.shop.common.exception.SthNotFoundException;
import com.shop.pojo.dto.OrderAllDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Order;
import com.shop.pojo.entity.OrderDetail;
import com.shop.pojo.entity.Prod;
import com.shop.pojo.vo.OrderGreatVO;
import com.shop.serve.mapper.OrderMapper;
import com.shop.serve.service.OrderDetailService;
import com.shop.serve.service.OrderService;
import com.shop.serve.service.ProdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.shop.common.constant.MessageConstants.BAD_ARGS;
import static com.shop.common.constant.MessageConstants.OBJECT_NOT_ALIVE;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ProdService prodService;

    @Override
    public OrderGreatVO orderDetail(OrderAllDTO orderAllDTO) {

        Order order = dtoFindEntity(orderAllDTO);

        OrderDetail orderDetail = orderDetailService.getOne(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getId, order.getId())
        );

        OrderGreatVO orderGreatVO = new OrderGreatVO();
        BeanUtils.copyProperties(order, orderGreatVO);
        BeanUtils.copyProperties(orderDetail, orderGreatVO);

        return orderGreatVO;
    }


    @Override
    @Transactional
    public void startOrder(ProdLocateDTO prodLocateDTO) {
        String name = prodLocateDTO.getName();
        Long userId = prodLocateDTO.getUserId();

        if (name == null || userId == null) throw new BadArgsException(BAD_ARGS);


        Prod prod = prodService.getOne(new LambdaQueryWrapper<Prod>()
                .eq(Prod::getName, name)
                .eq(Prod::getUserId, userId)
        );
        Long prod_id = prod.getId();

//        Long buyer_id = UserHolder.getUser().getId();
        // 调试选项
        Long buyer_id = 1L;
        Long seller_id = prod.getUserId();

        Order order = Order.builder()
                .buyerId(buyer_id)
                .sellerId(seller_id)
                .prodId(prod_id)
                .status(1) //买家开启交易后忽略传递时间, 直接进入等待卖家确认状态
                .build();
        this.save(order);

        OrderDetail orderDetail = OrderDetail.builder()
                .openTime(LocalDateTime.now())
                .build();
        orderDetailService.save(orderDetail);

    }


    @Override
    public void closeOrder(OrderAllDTO orderAllDTO) {
        Order order1 = dtoFindEntity(orderAllDTO);

        //保留式删除, 将订单状态置为5, 同时将订单详情的checkoutTime置为当前时间
        order1.setStatus(5);
        this.updateById(order1);

        OrderDetail orderDetail = orderDetailService.getOne(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getId, order1.getId())
        );
        orderDetail.setCheckoutTime(LocalDateTime.now());

        orderDetailService.updateById(orderDetail);

    }


    @Override
    public void sellerKnowAnswer(OrderAllDTO orderAllDTO) {
        Order order1 = dtoFindEntity(orderAllDTO);
        order1.setStatus(2);
        this.updateById(order1);
    }


    @Override
    public void buyerKnowAnswer(OrderAllDTO orderAllDTO) {
        Order order1 = dtoFindEntity(orderAllDTO);
        order1.setStatus(3);
        this.updateById(order1);
    }


    @Override
    public void sellerKnowClose(OrderAllDTO orderAllDTO) {
        Order order1 = dtoFindEntity(orderAllDTO);
        order1.setStatus(4);
        this.updateById(order1);

        //完成交易
        OrderDetail orderDetail = orderDetailService.getOne(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getId, order1.getId())
        );

        //订单详情的checkoutTime置为当前时间
        orderDetail.setCheckoutTime(LocalDateTime.now());
        orderDetailService.updateById(orderDetail);
        //TODO 其他完成交易后的操作
    }


    @Override
    public OrderGreatVO orderDetails(OrderAllDTO orderAllDTO) {
        Order order = dtoFindEntity(orderAllDTO);

        OrderDetail orderDetail = orderDetailService.getOne(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getId, order.getId())
        );

        OrderGreatVO orderGreatVO = new OrderGreatVO();
        BeanUtils.copyProperties(order, orderGreatVO);
        BeanUtils.copyProperties(orderDetail, orderGreatVO);
        return orderGreatVO;
    }


    /**
     * 根据订单DTO查找订单实体
     */
    private Order dtoFindEntity(OrderAllDTO orderAllDTO) {
        Long sellerId = orderAllDTO.getSellerId();
        Long buyerId = orderAllDTO.getBuyerId();

        if (sellerId == null || buyerId == null) throw new BadArgsException(BAD_ARGS);

        Order order1 = this.getOne(new LambdaQueryWrapper<Order>() //三个ID唯一确认订单
                .eq(Order::getBuyerId, orderAllDTO.getBuyerId())
                .eq(Order::getSellerId, orderAllDTO.getSellerId())
                .eq(Order::getProdId, orderAllDTO.getProdId())
        );

        if (order1 == null) throw new SthNotFoundException(OBJECT_NOT_ALIVE);
        return order1;
    }


}
