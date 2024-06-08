package com.shop.admin.task;

import com.shop.pojo.entity.ProdFunc;
import com.shop.pojo.entity.Voucher;
import com.shop.serve.service.HotsearchService;
import com.shop.serve.service.ProdService;
import com.shop.serve.service.VoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类，定时处理订单状态
 */
@Component
@Slf4j
public class VoucherTask {

    @Autowired
    private VoucherService voucherService;
    @Autowired
    private ProdService prodService;
    @Autowired
    private HotsearchService hotsearchService;

    /**
     * 处理到达失效时间的卷对象, 每天1点触发一次
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processUsingVoucher() {
        log.debug("定时判断所有当前使用中的卷对象是否到达失效时间：{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-60); //错开高峰期

        List<Voucher> voucherList = voucherService.getOutdateOnes(Voucher.USED, time);

        if (voucherList != null && !voucherList.isEmpty()) {
            for (Voucher voucher : voucherList) {
                voucherService.ruinVoucher(voucher); //失效对应券对象
            }
        }
    }

    /**
     * 处理到达失效展示时间的商品对象, 每天1点触发一次
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processShowingProd() {

        log.debug("定时判断所有当前展示中的商品对象是否到达展示失效时间：{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-60); //错开高峰期

        List<ProdFunc> prodFuncList = prodService.getOutdateOnes(time);

        if (prodFuncList != null && !prodFuncList.isEmpty()) {
            for (ProdFunc prodFunc : prodFuncList) {
                prodService.coolDownProd(prodFunc); //停止提升展示对应券对象
            }
        }
    }

    /**
     * 提取热搜表, 每天1点触发一次
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processHotSearch() {

        hotsearchService.clearAllHotSearch(); //清空热搜表

        log.debug("定时提取热搜表：{}", LocalDateTime.now());

        List<ProdFunc> prodFuncList = prodService.extractList4HotProd();

        if (prodFuncList != null && !prodFuncList.isEmpty()) {
            for (ProdFunc prodFunc : prodFuncList) {
                prodService.add2HotSearch(prodFunc); //添加到热搜表
            }
        }
    }

}
