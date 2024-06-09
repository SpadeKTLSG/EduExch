package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.VoucherAllDTO;
import com.shop.pojo.dto.VoucherLocateDTO;
import com.shop.pojo.entity.Voucher;

import java.time.LocalDateTime;
import java.util.List;

public interface VoucherService extends IService<Voucher> {

    void addSeckillVoucher(VoucherAllDTO voucherAllDTO);

    void addVoucher(VoucherAllDTO voucherAllDTO);

    void claimVoucher(VoucherLocateDTO voucherLocateDTO);

    Integer useVoucher4Seller(VoucherLocateDTO voucherLocateDTO);

    boolean useVoucher4Buyer(VoucherLocateDTO voucherLocateDTO);

    List<Voucher> getOutdateOnes(Integer status, LocalDateTime time);

    void ruinVoucher(Voucher voucher);

    Page<Voucher> searchVoucherByName(String name, Integer current);
}
