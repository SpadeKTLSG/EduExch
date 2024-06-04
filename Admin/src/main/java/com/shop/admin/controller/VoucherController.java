package com.shop.admin.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.common.utils.SystemConstants;
import com.shop.pojo.Result;
import com.shop.pojo.dto.VoucherAllDTO;
import com.shop.pojo.entity.Voucher;
import com.shop.serve.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     */
    @PostMapping("/add/seckill")
    @Operation(summary = "新增秒杀券")
    @Parameters(@Parameter(name = "voucherAllDTO", description = "优惠券添加DTO", required = true))
    public Result addSeckillVoucher(@RequestBody VoucherAllDTO voucherAllDTO) {
        Voucher voucher = new Voucher();
        BeanUtils.copyProperties(voucherAllDTO, voucher);
        voucher.setUserId(1L);
        voucher.setType(1);
        voucherService.save(voucher);
        return Result.success();
    }
    //http://localhost:8085/admin/voucher/add/seckill

    /**
     * 新增普通券
     */
    @PostMapping("/add")
    @Operation(summary = "新增普通券")
    @Parameters(@Parameter(name = "voucherAllDTO", description = "优惠券添加DTO", required = true))
    public Result addVoucher(@RequestBody VoucherAllDTO voucherAllDTO) {
        Voucher voucher = new Voucher();
        BeanUtils.copyProperties(voucherAllDTO, voucher);
        voucher.setUserId(1L); //存到默认仓库用户 TODO 公共字段
        voucherService.save(voucher);
        return Result.success();
    }
    //http://localhost:8085/admin/voucher/add


    //! DELETE
    //禁止


    //! UPDATE
    //禁止

    //! QUERY

    /**
     * 分页查询软件的优惠券列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询软件的优惠券列表")
    @Parameters(@Parameter(name = "current", description = "当前页", required = true))
    public Result pageVoucher(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return Result.success(voucherService.page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE)));
    }
    //http://localhost:8085/admin/voucher/page

}
