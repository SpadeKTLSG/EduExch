package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.constant.SystemConstants;
import com.shop.common.exception.AccountNotFoundException;
import com.shop.common.exception.SthNotFoundException;
import com.shop.pojo.dto.ProdAllDTO;
import com.shop.pojo.dto.ProdFuncAllDTO;
import com.shop.pojo.dto.ProdGreatDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Prod;
import com.shop.pojo.entity.ProdFunc;
import com.shop.serve.mapper.ProdMapper;
import com.shop.serve.service.ProdCateService;
import com.shop.serve.service.ProdFuncService;
import com.shop.serve.service.ProdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.shop.common.constant.MessageConstants.ACCOUNT_NOT_FOUND;
import static com.shop.common.constant.MessageConstants.OBJECT_NOT_ALIVE;
import static com.shop.common.utils.NewBeanUtils.dtoMapService;

@Slf4j
@Service
public class ProdServiceImpl extends ServiceImpl<ProdMapper, Prod> implements ProdService {

    @Autowired
    private ProdFuncService prodFuncService;
    @Autowired
    private ProdCateService prodCateService;

    @Override
    @Transactional
    public void update4User(ProdGreatDTO prodGreatDTO) throws InstantiationException, IllegalAccessException {
        //? 联表选择性更新字段
        Optional<Prod> optionalProd = Optional.ofNullable(this.getOne(Wrappers.<Prod>lambdaQuery().eq(Prod::getName, prodGreatDTO.getName())));
        if (optionalProd.isEmpty()) throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);

        //这里商品的userId是自己
//        optionalProd.get().setUserId(UserHolder.getUser().getId());
        // 调试选项
        optionalProd.get().setUserId(1L);

        Map<Object, IService> dtoServiceMap = new HashMap<>();
        dtoServiceMap.put(createDTOFromProdGreatDTO(prodGreatDTO, ProdAllDTO.class), this);
        dtoServiceMap.put(createDTOFromProdGreatDTO(prodGreatDTO, ProdFuncAllDTO.class), prodFuncService);

        dtoMapService(dtoServiceMap, optionalProd.get().getId(), optionalProd);
    }


    @Override
    public void check(ProdLocateDTO prodLocateDTO) {
        if (this.query().eq("name", prodLocateDTO.getName()).count() == 0) throw new SthNotFoundException(OBJECT_NOT_ALIVE);

        if (this.query().eq("user_id", prodLocateDTO.getUserId()).count() == 0) throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);


        Prod prod = this.getOne(new LambdaQueryWrapper<Prod>()// 找到对应商品id, 通过id找到另一张表UserFunc, 修改状态字段
                .eq(Prod::getName, prodLocateDTO.getName())
                .eq(Prod::getUserId, prodLocateDTO.getUserId())
        );

        ProdFunc prodFunc = prodFuncService.getOne(new LambdaQueryWrapper<ProdFunc>()
                .eq(ProdFunc::getId, prod.getId())
        );

        prodFunc.setStatus(1);
        prodFuncService.updateById(prodFunc);
    }

    @Override
    public Page<ProdGreatDTO> page2Check(Integer current) {
        Page<ProdFunc> prodFuncPage = prodFuncService.page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE),
                new LambdaQueryWrapper<ProdFunc>().eq(ProdFunc::getStatus, 0));

        List<ProdGreatDTO> mergedList = new ArrayList<>();

        for (ProdFunc prodFunc : prodFuncPage.getRecords()) {
            Prod prod = this.getById(prodFunc.getId());
            if (prod != null) {
                ProdGreatDTO prodGreatDTO = new ProdGreatDTO();
                BeanUtils.copyProperties(prod, prodGreatDTO);
                BeanUtils.copyProperties(prodFunc, prodGreatDTO);
                mergedList.add(prodGreatDTO);
            }
        }

        Page<ProdGreatDTO> mergedPage = new Page<>(current, SystemConstants.MAX_PAGE_SIZE);
        mergedPage.setRecords(mergedList);
        mergedPage.setTotal(mergedList.size());

        return mergedPage;
    }

    @Override
    public void deleteByNameUser(ProdLocateDTO prodLocateDTO) {
        if (this.query().eq("name", prodLocateDTO.getName()).count() == 0) throw new SthNotFoundException(OBJECT_NOT_ALIVE);

        if (this.query().eq("user_id", prodLocateDTO.getUserId()).count() == 0) throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);

        this.remove(new LambdaQueryWrapper<Prod>()
                .eq(Prod::getName, prodLocateDTO.getName())
                .eq(Prod::getUserId, prodLocateDTO.getUserId())
        );
    }

    @Override
    public Prod getByNameUser(ProdLocateDTO prodLocateDTO) {
        if (this.query().eq("name", prodLocateDTO.getName()).count() == 0) throw new SthNotFoundException(OBJECT_NOT_ALIVE);

        if (this.query().eq("user_id", prodLocateDTO.getUserId()).count() == 0) throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);

        return this.getOne(new LambdaQueryWrapper<Prod>()
                .eq(Prod::getName, prodLocateDTO.getName())
                .eq(Prod::getUserId, prodLocateDTO.getUserId())
        );
    }


    /**
     * 从ProdGreatDTO创建DTO
     */
    @SuppressWarnings("deprecation")
    private <T> T createDTOFromProdGreatDTO(ProdGreatDTO prodGreatDTO, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        T dto = clazz.newInstance();
        BeanUtils.copyProperties(prodGreatDTO, dto);
        return dto;
    }
}
