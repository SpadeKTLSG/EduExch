package com.shop.admin.controller;


import com.shop.pojo.Result;
import com.shop.pojo.entity.Voucher;
import com.shop.serve.service.VoucherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/admin/voucher")
@RestController
public class VoucherController {


    @Autowired
    private VoucherService voucherService;

    //! Func


    //! ADD

    /**
     * 新增秒杀券
     *
     * @param voucher 优惠券信息，包含秒杀信息
     * @return 优惠券id
     */
    @PostMapping("seckill")
    public Result addSeckillVoucher(@RequestBody Voucher voucher) {
//        voucherService.addSeckillVoucher(voucher);
        return Result.success();
    }

    /**
     * 新增普通券
     *
     * @param voucher 优惠券信息
     * @return 优惠券id
     */
    @PostMapping
    public Result addVoucher(@RequestBody Voucher voucher) {
//        voucherService.save(voucher);
        return Result.success();
    }


    //! DELETE
    //禁止


    //! UPDATE
    //禁止

    //! QUERY

    /**
     * 查询软件的优惠券列表
     */

}
