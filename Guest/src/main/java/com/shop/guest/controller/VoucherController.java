package com.shop.guest.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.pojo.Result;
import com.shop.pojo.dto.VoucherLocateDTO;
import com.shop.pojo.dto.VoucherStoreDTO;
import com.shop.pojo.entity.Order;
import com.shop.pojo.entity.UserFunc;
import com.shop.pojo.entity.Voucher;
import com.shop.pojo.vo.VoucherStoreVO;
import com.shop.serve.service.OrderService;
import com.shop.serve.service.UserFuncService;
import com.shop.serve.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.shop.common.constant.MyConstants.*;
import static com.shop.common.constant.SystemConstants.MAX_PAGE_SIZE;

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
    @Autowired
    private UserFuncService userFuncService;
    @Autowired
    private OrderService orderService;

    //! Func


    //! ADD
    //禁止


    //! DELETE


    /**
     * 使用自己的卖方优惠券
     * <p>进行优惠券功能时需要判断权限和对象</p>
     * <p>保留式删除</p>
     */
    @Transactional
    @DeleteMapping("/use/seller")
    public Result useVoucher4Seller(@RequestBody VoucherStoreDTO voucherStoreDTO) {

        Voucher voucher = voucherService.getOne(new LambdaQueryWrapper<Voucher>()
                .eq(Voucher::getName, voucherStoreDTO.getName())
//                .eq(Voucher::getUserId, UserHolder.getUser().getId()));
                // 调试选项
                .eq(Voucher::getUserId, SELLER_USERID));

        if (voucher == null) {
            return Result.error("优惠券不存在");
        }
        if (voucher.getStatus() == 1 || voucher.getStatus() == 2 || voucher.getStock() == 0) {
            return Result.error("优惠券已使用或已过期(指使用后持续时间结束)");
        }
        if (voucher.getUser() == 1) {
            return Result.error("参数错误");
        }


        //执行功能
        voucher.setStatus(1);
        voucher.setStock(0);
        voucher.setBeginTime(LocalDateTime.now());

        if (voucher.getFunc() == 0) { //基础功能类型
            voucher.setEndTime(LocalDateTime.now().plusDays(1)); // 1天
        } else if (voucher.getFunc() == 1) { //高级功能类型
            voucher.setEndTime(LocalDateTime.now().plusDays(3)); // 3天
        } else if (voucher.getFunc() == 2) { //超级功能类型
            voucher.setEndTime(LocalDateTime.now().plusDays(7)); // 7天
        }


        //UserFunc userFunc = userFuncService.getById(UserHolder.getUser().getId());
        //调试选项

        UserFunc userFunc = userFuncService.getById(SELLER_USERID);
        int value2Add = voucher.getType() == 0 ? voucher.getValue() : voucher.getValue() * 2;

        userFunc.setCredit(userFunc.getCredit() + value2Add);
        userFuncService.updateById(userFunc);

        //回传给前端效果字段 : voucher.getFunc(), 0 - 1 - 2 指明其功能类型, 用于后续前端打开窗口,
        // 让用户指定商品对象进行Update操作-> ProdFunc字段修改, 请求见ProdController

        return Result.success(voucher.getFunc());
    }


    /**
     * 使用自己的买方优惠券 -> 下一步直接发起交易,
     * <p>进行优惠券功能时需要判断权限和对象</p>
     * <p>保留式删除</p>
     */
    @Transactional
    @DeleteMapping("/use/buyer")
    public Result useVoucher4Buyer(@RequestBody VoucherStoreDTO voucherStoreDTO) {

        Voucher voucher = voucherService.getOne(new LambdaQueryWrapper<Voucher>()
                .eq(Voucher::getName, voucherStoreDTO.getName())
//                .eq(Voucher::getUserId, UserHolder.getUser().getId()));
                // 调试选项
                .eq(Voucher::getUserId, BUYER_USERID));

        if (voucher == null) {
            return Result.error("优惠券不存在");
        }
        if (voucher.getStatus() == 1 || voucher.getStatus() == 2 || voucher.getStock() == 0) {
            return Result.error("优惠券已使用或已过期");
        }
        if (voucher.getUser() == 1) {
            return Result.error("参数错误");
        }


        //执行功能
        voucher.setStatus(1);
        voucher.setStock(0);
        voucher.setBeginTime(LocalDateTime.now());

        if (voucher.getFunc() == 0) { //基础功能类型
            voucher.setEndTime(LocalDateTime.now().plusDays(1)); // 1天
        } else if (voucher.getFunc() == 1) { //高级功能类型
            voucher.setEndTime(LocalDateTime.now().plusDays(3)); // 3天
        } else if (voucher.getFunc() == 2) { //超级功能类型
            voucher.setEndTime(LocalDateTime.now().plusDays(7)); // 7天
        }


        //UserFunc userFunc = userFuncService.getById(UserHolder.getUser().getId());
        //调试选项

        UserFunc userFunc = userFuncService.getById(BUYER_USERID);
        int value2Add = voucher.getType() == 0 ? voucher.getValue() : voucher.getValue() * 2;

        userFunc.setCredit(userFunc.getCredit() + value2Add);
        userFuncService.updateById(userFunc);

        //后续: 直接对目前开启的交易判定是否存在, 存在则视为一次准入成功, 对用户进行增加嘉奖值操作(否则没有奖励)


        Order order = orderService.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getBuyerId, BUYER_USERID));

        if (order != null) {
            UserFunc userFunc2 = userFuncService.getById(BUYER_USERID);
            userFunc2.setGodhit(userFunc2.getGodhit() + 1);
            userFuncService.updateById(userFunc2);
            return Result.success("交易成功, 奖励10嘉奖值");
        }

        return Result.success("还没有进行交易, 奖励无发放");
    }


    //! UPDATE

    //后续引入秒杀流程, 管理员发布后执行


    /**
     * 宣称(领取)优惠券
     * <p>不可重复领取</p>
     */
    @PutMapping("/claim")
    @Operation(summary = "宣称(领取)优惠券")
    @Parameters(@Parameter(name = "voucherLocateDTO", description = "优惠券定位DTO", required = true))
    public Result claimVoucher(@RequestBody VoucherLocateDTO voucherLocateDTO) {

        Voucher voucher = voucherService.getOne(new LambdaQueryWrapper<Voucher>()
                .eq(Voucher::getName, voucherLocateDTO.getName())
                .eq(Voucher::getUserId, STORE_USERID));

        if (voucher == null) {
            return Result.error("优惠券不存在");
        }


//        voucher.setUserId(UserHolder.getUser().getId());
        // 调试选项
        voucher.setUserId(BUYER_USERID);
        //数量调整: 原来的数量是指仓库管理员持有的数量, 这里是用户持有的数量, 限制只能同种的一张
        voucher.setStock(1);
        voucherService.updateById(voucher);

        return Result.success();
    }
    //http://localhost:8086/guest/voucher/claim


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
                        .eq(Voucher::getUser, STORE_USERID))
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
                        .eq(Voucher::getUser, STORE_USERID))
                .convert(voucher -> {
                    VoucherStoreVO voucherStoreVO = new VoucherStoreVO();
                    BeanUtils.copyProperties(voucher, voucherStoreVO);
                    return voucherStoreVO;
                }));
    }
    //http://localhost:8086/guest/voucher/page/buyer


    /**
     * 分页查询自己的卖方优惠券列表
     * <p>之后点击可以使用</p>
     */
    @GetMapping("/me/page/seller")
    @Operation(summary = "分页自己卖方优惠券列表")
    @Parameters(@Parameter(name = "current", description = "当前页", required = true))
    public Result pageMyVoucher4Seller(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return Result.success(voucherService.page(new Page<>(current, MAX_PAGE_SIZE), new LambdaQueryWrapper<Voucher>()
                        .eq(Voucher::getUser, STORE_USERID)
                        //                        .eq(Voucher::getUserId, UserHolder.getUser().getId()))
                        // 调试选项
                        .eq(Voucher::getUserId, SELLER_USERID))
                .convert(voucher -> {
                    VoucherStoreVO voucherStoreVO = new VoucherStoreVO();
                    BeanUtils.copyProperties(voucher, voucherStoreVO);
                    return voucherStoreVO;
                }));
    }
    //http://localhost:8086/guest/voucher/me/page/seller


    /**
     * 分页查询自己的买方优惠券列表
     * <p>之后点击可以使用</p>
     */
    @GetMapping("/me/page/buyer")
    @Operation(summary = "分页自己买方优惠券列表")
    @Parameters(@Parameter(name = "current", description = "当前页", required = true))
    public Result pageMyVoucher4Buyer(@RequestParam(value = "current", defaultValue = "1") Integer current) {

        return Result.success(voucherService.page(new Page<>(current, MAX_PAGE_SIZE), new LambdaQueryWrapper<Voucher>()
                        .eq(Voucher::getUser, STORE_USERID)
//                        .eq(Voucher::getUserId, UserHolder.getUser().getId()))
                        // 调试选项
                        .eq(Voucher::getUserId, BUYER_USERID))


                .convert(voucher -> {
                    VoucherStoreVO voucherStoreVO = new VoucherStoreVO();
                    BeanUtils.copyProperties(voucher, voucherStoreVO);
                    return voucherStoreVO;
                }));
    }
    //http://localhost:8086/guest/voucher/me/page/buyer

}
