package com.shop.guest.controller;


import com.shop.serve.service.VoucherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 优惠券控制
 *
 * @author SK
 * @date 2024/06/03
 */
@Slf4j
@Tag(name = "Voucher", description = "优惠券")
@RequestMapping("/guest/voucher")
@RestController
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    //! Func


    //! ADD
    //禁止

    //! DELETE
    //禁止


    //! UPDATE


    /**
     * 宣称优惠券
     * <p>不可重复领取</p>
     */

    /**
     * 使用优惠券
     * <p>进行优惠券功能时需要判断权限和对象</p>
     */

    //! QUERY

    /**
     * 查询软件的优惠券列表
     * <p>可以点击领取(一出就可以)</p>
     */
}
