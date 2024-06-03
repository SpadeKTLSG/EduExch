package com.shop.guest.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.pojo.Result;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Order;
import com.shop.pojo.entity.OrderDetail;
import com.shop.pojo.entity.Prod;
import com.shop.serve.service.OrderDetailService;
import com.shop.serve.service.OrderService;
import com.shop.serve.service.ProdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
