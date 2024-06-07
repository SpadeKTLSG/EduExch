package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.VoucherAllDTO;
import com.shop.pojo.dto.VoucherLocateDTO;
import com.shop.pojo.dto.VoucherStoreDTO;
import com.shop.pojo.entity.Voucher;

import java.time.LocalDateTime;
import java.util.List;

public interface VoucherService extends IService<Voucher> {

    void addSeckillVoucher(VoucherAllDTO voucherAllDTO);

    void addVoucher(VoucherAllDTO voucherAllDTO);

    void claimVoucher(VoucherLocateDTO voucherLocateDTO);

    Integer useVoucher4Seller(VoucherStoreDTO voucherStoreDTO);

    boolean useVoucher4Buyer(VoucherStoreDTO voucherStoreDTO);

    List<Voucher> getOutdateOnes(Integer status, LocalDateTime time);

    void ruinVoucher(Voucher voucher);
}
