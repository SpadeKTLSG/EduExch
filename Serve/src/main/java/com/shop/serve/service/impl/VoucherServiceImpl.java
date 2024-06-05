package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.pojo.dto.VoucherAllDTO;
import com.shop.pojo.entity.Voucher;
import com.shop.serve.mapper.VoucherMapper;
import com.shop.serve.service.VoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher> implements VoucherService {

    @Override
    public void addSeckillVoucher(VoucherAllDTO voucherAllDTO) {
        Voucher voucher = new Voucher();
        BeanUtils.copyProperties(voucherAllDTO, voucher);
        voucher.setUserId(1L);
        voucher.setType(1);
        this.save(voucher);
    }

    @Override
    public void addVoucher(VoucherAllDTO voucherAllDTO) {
        Voucher voucher = new Voucher();
        BeanUtils.copyProperties(voucherAllDTO, voucher);
        voucher.setUserId(1L); //存到默认仓库用户 TODO 公共字段
        this.save(voucher);
    }
}
