package com.shop.guest.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.pojo.Result;
import com.shop.pojo.entity.Voucher;
import com.shop.pojo.vo.VoucherStoreVO;
import com.shop.serve.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.shop.common.utils.SystemConstants.MAX_PAGE_SIZE;

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
     * 宣称(领取)优惠券
     * <p>不可重复领取</p>
     */

    /**
     * 使用优惠券
     * <p>进行优惠券功能时需要判断权限和对象</p>
     */

    //! QUERY

    /**
     * 分页查询仓库里卖方优惠券列表
     * <p>之后点击可以领取(一出就可以)</p>
     */
    @GetMapping("/page/seller")
    @Operation(summary = "分页仓库里卖方优惠券列表")
    @Parameters(@Parameter(name = "current", description = "当前页", required = true))
    public Result pageVoucher4Seller(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return Result.success(voucherService.page(new Page<>(current, MAX_PAGE_SIZE), new LambdaQueryWrapper<Voucher>()
                        .eq(Voucher::getUser, 0))
                .convert(voucher -> {
                    VoucherStoreVO voucherStoreVO = new VoucherStoreVO();
                    BeanUtils.copyProperties(voucher, voucherStoreVO);
                    return voucherStoreVO;
                }));
    }
    //http://localhost:8086/guest/voucher/page/seller


    /**
     * 分页查询仓库里买方优惠券列表
     * <p>之后点击可以领取(一出就可以)</p>
     */
    @GetMapping("/page/buyer")
    @Operation(summary = "分页仓库里买方优惠券列表")
    @Parameters(@Parameter(name = "current", description = "当前页", required = true))
    public Result pageVoucher4Buyer(@RequestParam(value = "current", defaultValue = "1") Integer current) {

        return Result.success(voucherService.page(new Page<>(current, MAX_PAGE_SIZE), new LambdaQueryWrapper<Voucher>()
                        .eq(Voucher::getUser, 1))
                .convert(voucher -> {
                    VoucherStoreVO voucherStoreVO = new VoucherStoreVO();
                    BeanUtils.copyProperties(voucher, voucherStoreVO);
                    return voucherStoreVO;
                }));
    }
    //http://localhost:8086/guest/voucher/page/buyer
}
