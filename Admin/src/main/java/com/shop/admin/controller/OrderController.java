package com.shop.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.common.constant.SystemConstants;
import com.shop.pojo.Result;
import com.shop.pojo.dto.OrderAllDTO;
import com.shop.pojo.entity.Order;
import com.shop.pojo.entity.OrderDetail;
import com.shop.pojo.vo.OrderGreatVO;
import com.shop.serve.service.OrderDetailService;
import com.shop.serve.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制
 *
 * @author SK
 * @date 2024/06/03
 */
@Slf4j
@Tag(name = "Order", description = "订单")
@RequestMapping("/admin/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;


    //! Func


    //! ADD
    //禁止

    //! DELETE
    //禁止

    //! UPDATE
    //禁止

    //! QUERY

    /**
     * 分页查看所有订单列表
     */
    @GetMapping("/listall")
    @Operation(summary = "查看所有订单列表")
    @Parameters(@Parameter(name = "current", description = "当前页", required = true))
    public Result orderPage(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return Result.success(orderService.page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE)));
    }
    //http://localhost:8085/admin/order/listall


    /**
     * 查看一个订单详情
     * <p>联表</p>
     */
    @GetMapping("/detail")
    public Result orderDetail(@RequestBody OrderAllDTO orderAllDTO) {
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
    //http://localhost:8085/admin/order/detail

}
