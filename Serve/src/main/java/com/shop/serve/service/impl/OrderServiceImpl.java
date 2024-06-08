package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.util.concurrent.RateLimiter;
import com.shop.common.context.UserHolder;
import com.shop.common.exception.BadArgsException;
import com.shop.common.exception.NetWorkException;
import com.shop.common.exception.SthNotFoundException;
import com.shop.pojo.dto.OrderAllDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.*;
import com.shop.pojo.vo.OrderGreatVO;
import com.shop.serve.mapper.OrderMapper;
import com.shop.serve.mq.MQSender;
import com.shop.serve.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.shop.common.constant.MessageConstant.*;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ProdService prodService;
    @Autowired
    private ProdFuncService prodFuncService;
    @Autowired
    private UserFuncService userFuncService;

    @Autowired
    private MQSender mqSender;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 令牌桶算法 限流
     */
    private RateLimiter rateLimiter = RateLimiter.create(10);


    /**
     * lua脚本对象
     */
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }


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

        if (prod == null || prod.getStock() <= 0) throw new SthNotFoundException(OBJECT_NOT_ALIVE);


        ProdFunc prodFunc = prodFuncService.getOne(new LambdaQueryWrapper<ProdFunc>()
                .eq(ProdFunc::getId, prod.getId())
        );
        if (!Objects.equals(prodFunc.getStatus(), ProdFunc.NORMAL)) throw new BadArgsException(BAD_ARGS);      //审核未通过的商品不可交易


        //创建订单流程
        prod.setStock(prod.getStock() - 1); //库存减一

        Long prod_id = prod.getId();
        Long buyer_id = UserHolder.getUser().getId();
        Long seller_id = prod.getUserId();

        Order order = Order.builder()
                .buyerId(buyer_id)
                .sellerId(seller_id)
                .prodId(prod_id)
                .status(Order.WAITCHECK) //模拟: 买家开启交易后忽略传递时间, 直接进入等待卖家确认状态
                .build();
        this.save(order);

        OrderDetail orderDetail = OrderDetail.builder()
                .openTime(LocalDateTime.now())
                .build();
        orderDetailService.save(orderDetail);

        prodService.updateById(prod);
    }


    /**
     * ! 秒杀下单流程 TODO
     */
    @Override
    public void seckillStartOrder(ProdLocateDTO prodLocateDTO) {

        //令牌桶算法进行限流
        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) throw new NetWorkException(NETWORK_ERROR);

        // 执行流程
        // 构造输入参数
        Long userId = UserHolder.getUser().getId();

        Long r = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(),
                userId.toString()
        );

    }


    @Override
    @Transactional
    public void closeOrder(OrderAllDTO orderAllDTO) {
        Order order1 = dtoFindEntity(orderAllDTO);

        //保留式删除, 将订单状态置为5, 同时将订单详情的checkoutTime置为当前时间
        order1.setStatus(Order.STOP);
        this.updateById(order1);

        OrderDetail orderDetail = orderDetailService.getOne(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getId, order1.getId())
        );
        orderDetail.setCheckoutTime(LocalDateTime.now());

        orderDetailService.updateById(orderDetail);

    }


    @Override
    @Transactional
    public void sellerKnowAnswer(OrderAllDTO orderAllDTO) {
        Order order1 = dtoFindEntity(orderAllDTO);
        //限制上一个状态为等待卖家确认
        if (!Objects.equals(order1.getStatus(), Order.WAITCHECK)) throw new BadArgsException(BAD_ARGS);
        order1.setStatus(Order.TALKING);
        this.updateById(order1);
    }


    @Override
    @Transactional
    public void buyerKnowAnswer(OrderAllDTO orderAllDTO) {
        Order order1 = dtoFindEntity(orderAllDTO);
        //限制上一个状态为交涉中
        if (!Objects.equals(order1.getStatus(), Order.TALKING)) throw new BadArgsException(BAD_ARGS);
        order1.setStatus(Order.EXCHANGING);
        this.updateById(order1);
    }


    @Override
    @Transactional
    public void sellerKnowClose(OrderAllDTO orderAllDTO) {
        Order order1 = dtoFindEntity(orderAllDTO);
        //限制上一个状态为正在交易
        if (!Objects.equals(order1.getStatus(), Order.EXCHANGING)) throw new BadArgsException(BAD_ARGS);
        order1.setStatus(Order.OVER);
        this.updateById(order1);

        //完成交易
        OrderDetail orderDetail = orderDetailService.getOne(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getId, order1.getId())
        );

        orderDetail.setCheckoutTime(LocalDateTime.now());//订单详情的checkoutTime置为当前时间
        orderDetailService.updateById(orderDetail);

        //修改三方字段: 买家和卖家的对应交易次数+1
        UserFunc buyerFunc = userFuncService.getById(order1.getBuyerId());
        UserFunc sellerFunc = userFuncService.getById(order1.getSellerId());
        buyerFunc.setGains(buyerFunc.getGains() + 1);
        sellerFunc.setSolds(buyerFunc.getSolds() + 1);

        userFuncService.updateById(buyerFunc);
        userFuncService.updateById(sellerFunc);
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
