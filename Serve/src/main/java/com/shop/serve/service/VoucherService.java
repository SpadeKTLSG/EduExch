package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.VoucherAllDTO;
import com.shop.pojo.entity.Voucher;

public interface VoucherService extends IService<Voucher> {
    void addSeckillVoucher(VoucherAllDTO voucherAllDTO);

    void addVoucher(VoucherAllDTO voucherAllDTO);
}
