package com.shop.guest.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.common.constant.SystemConstants;
import com.shop.pojo.Result;
import com.shop.pojo.dto.OrderAllDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Order;
import com.shop.serve.service.OrderDetailService;
import com.shop.serve.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单
 *
 * @author SK
 * @date 2024/06/03
 */
@Slf4j
@Tag(name = "Order", description = "订单")
@RequestMapping("/guest/order")
@RestController
public class OrderController {


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
        orderService.startOrder(prodLocateDTO);
        return Result.success();
    }
    //http://localhost:8086/guest/order/start


    //! DELETE


    /**
     * 终止当前交易(任何阶段)(买家或是卖家)
     */
    @DeleteMapping("/stop")
    @Operation(summary = "终止当前交易")
    @Parameters(@Parameter(name = "orderAllDTO", description = "订单DTO", required = true))
    public Result stopOrder(@RequestBody OrderAllDTO orderAllDTO) {
        orderService.closeOrder(orderAllDTO);
        return Result.success();
    }
    //http://localhost:8086/guest/order/stop


    //! UPDATE


    /**
     * 卖家确认, 之后进入交涉中状态
     */
    @PutMapping("/confirm/seller/answer")
    @Operation(summary = "卖家确认")
    @Parameters(@Parameter(name = "orderAllDTO", description = "订单DTO", required = true))
    public Result sellerKnowAnswer(@RequestBody OrderAllDTO orderAllDTO) {
        orderService.sellerKnowAnswer(orderAllDTO);
        return Result.success();
    }
    //http://localhost:8086/guest/order/confirm/seller/answer


    /**
     * 双方交涉完毕后买家确认, 之后进入正在交易状态
     */
    @PutMapping("/confirm/buyer/answer")
    @Operation(summary = "交涉完毕买家确认")
    @Parameters(@Parameter(name = "orderAllDTO", description = "订单DTO", required = true))
    public Result buyerKnowAnswer(@RequestBody OrderAllDTO orderAllDTO) {
        orderService.buyerKnowAnswer(orderAllDTO);
        return Result.success();
    }
    //http://localhost:8086/guest/order/confirm/buyer/answer


    /**
     * 买家确认交易后自行与卖家交易, 交易完成后卖方确认交易完成
     * <p>双方交易完成, 之后进入交易完成状态(封存关闭交易)</p>
     */
    @PutMapping("/confirm/seller/close")
    @Operation(summary = "卖家确认交易完成")
    @Parameters(@Parameter(name = "orderAllDTO", description = "订单DTO", required = true))
    public Result sellerKnowClose(@RequestBody OrderAllDTO orderAllDTO) {
        orderService.sellerKnowClose(orderAllDTO);
        return Result.success();
    }
    //http://localhost:8086/guest/order/confirm/seller/close


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
    @GetMapping("/detail")
    @Operation(summary = "查看一个订单详情")
    @Parameters(@Parameter(name = "orderAllDTO", description = "订单DTO", required = true))
    public Result orderDetails(@RequestBody OrderAllDTO orderAllDTO) {
        return Result.success(orderService.orderDetails(orderAllDTO));
    }
    //http://localhost:8086/guest/order/detail


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
