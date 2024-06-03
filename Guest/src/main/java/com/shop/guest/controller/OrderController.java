package com.shop.guest.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.common.utils.SystemConstants;
import com.shop.pojo.Result;
import com.shop.pojo.dto.OrderAllDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Order;
import com.shop.pojo.entity.OrderDetail;
import com.shop.pojo.entity.Prod;
import com.shop.pojo.vo.OrderGreatVO;
import com.shop.serve.service.OrderDetailService;
import com.shop.serve.service.OrderService;
import com.shop.serve.service.ProdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@Tag(name = "Order", description = "订单")
@RequestMapping("/guest/order")
@RestController
public class OrderController {

    @Autowired
    private ProdService prodService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;


    //! Func

    //支付模块: 委托外部支付平台进行支付(未实现)


    //! ADD


    /**
     * 用户开启交易
     */
    @PostMapping("/start")
    @Operation(summary = "用户开启交易")
    @Parameters(@Parameter(name = "prodLocateDTO", description = "商品定位DTO", required = true))
    public Result startOrder(@RequestBody ProdLocateDTO prodLocateDTO) {

        String name = prodLocateDTO.getName();
        Long userId = prodLocateDTO.getUserId();

        if (name == null || userId == null) {
            return Result.error("参数错误");
        }

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
        orderService.save(order);

        OrderDetail orderDetail = OrderDetail.builder()
                .openTime(LocalDateTime.now())
                .build();
        orderDetailService.save(orderDetail);

        return Result.success();
    }
    //http://localhost:8086/guest/order/start


    //! DELETE


    /**
     * 终止当前交易
     */


    //! UPDATE


    /**
     * 卖家确认, 之后进入交涉中状态
     */

    /**
     * 双方交涉完毕确认, 之后进入正在交易状态
     */

    /**
     * 双方交易完成, 之后进入交易完成状态(封存关闭交易)
     */


    //! QUERY

    /**
     * 分页查看自己的订单列表, 简要信息
     */
    @GetMapping("/list")
    @Operation(summary = "分页查看自己的订单列表")
    @Parameters(@Parameter(name = "current", description = "当前页", required = true))
    public Result orderPage(@RequestParam(value = "current", defaultValue = "1") Integer current) {

//        Long userId = UserHolder.getUser().getId();
        // 调试选项
        Long userId = 1L;

        return Result.success(orderService.page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE),
                Wrappers.<Order>lambdaQuery()
                        .eq(Order::getBuyerId, userId)
                        .or()
                        .eq(Order::getSellerId, userId)
        ));
    }
    //http://localhost:8086/guest/order/list


    /**
     * 查看一个订单详情
     * <p>联表</p>
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "查看一个订单详情")
    @Parameters(@Parameter(name = "id", description = "订单ID", required = true))
    public Result orderDetails(@RequestBody OrderAllDTO orderAllDTO) {

        Long sellerId = orderAllDTO.getSellerId();
        Long buyerId = orderAllDTO.getBuyerId();

        if (sellerId == null || buyerId == null) {
            return Result.error("参数错误");
        }

        Order order = orderService.getOne(new LambdaQueryWrapper<Order>() //三个ID唯一确认订单
                .eq(Order::getBuyerId, orderAllDTO.getBuyerId())
                .eq(Order::getSellerId, orderAllDTO.getSellerId())
                .eq(Order::getProdId, orderAllDTO.getProdId())
        );

        if (order == null) {
            return Result.error("订单不存在");
        }

        OrderDetail orderDetail = orderDetailService.getOne(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getId, order.getId())
        );

        OrderGreatVO orderGreatVO = new OrderGreatVO();
        BeanUtils.copyProperties(order, orderGreatVO);
        BeanUtils.copyProperties(orderDetail, orderGreatVO);

        return Result.success(orderGreatVO);
    }
    //http://localhost:8086/guest/order/detail/1


    /**
     * 计数关于自己的各种状态的订单
     * <p>用于前端展示</p>
     */
    @GetMapping("/status/count/{status}")
    @Operation(summary = "计数自己各种状态的订单")
    @Parameters(@Parameter(name = "status", description = "订单状态", required = true))
    public Result orderStatusCount(@PathVariable Integer status) {

        return Result.success(orderService.count(new LambdaQueryWrapper<Order>()
                        .eq(Order::getStatus, status)
//                .and(i -> i.eq(Order::getBuyerId, UserHolder.getUser().getId()).or().eq(Order::getSellerId, UserHolder.getUser().getId()))
                        // 调试选项
                        .and(i -> i.eq(Order::getBuyerId, 1L).or().eq(Order::getSellerId, 1L))
        ));
    }
    //http://localhost:8086/guest/order/status/count/1

}
