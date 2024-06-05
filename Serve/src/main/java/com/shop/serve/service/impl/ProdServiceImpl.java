package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.constant.SystemConstants;
import com.shop.common.exception.*;
import com.shop.pojo.dto.ProdAllDTO;
import com.shop.pojo.dto.ProdFuncAllDTO;
import com.shop.pojo.dto.ProdGreatDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Prod;
import com.shop.pojo.entity.ProdCate;
import com.shop.pojo.entity.ProdFunc;
import com.shop.pojo.vo.ProdGreatVO;
import com.shop.serve.mapper.ProdMapper;
import com.shop.serve.service.ProdCateService;
import com.shop.serve.service.ProdFuncService;
import com.shop.serve.service.ProdService;
import com.shop.serve.tool.NewDTOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.shop.common.constant.MessageConstants.*;
import static com.shop.common.utils.NewBeanUtils.dtoMapService;

@Slf4j
@Service
public class ProdServiceImpl extends ServiceImpl<ProdMapper, Prod> implements ProdService {

    @Autowired
    private ProdFuncService prodFuncService;
    @Autowired
    private ProdCateService prodCateService;
    @Autowired
    private NewDTOUtils dtoUtils;


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


    @Override
    public Page<ProdGreatDTO> pageProd(Integer current) {

        Page<Prod> prodPage = this.page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        Page<ProdFunc> prodFuncPage = prodFuncService.page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        List<ProdGreatDTO> mergedList = new ArrayList<>(); // 存储合并后的结果

        for (int i = 0; i < prodPage.getRecords().size(); i++) {
            Prod prod = prodPage.getRecords().get(i);
            ProdFunc prodFunc = prodFuncPage.getRecords().get(i);

            ProdGreatDTO prodGreatDTO = new ProdGreatDTO();
            BeanUtils.copyProperties(prod, prodGreatDTO);
            BeanUtils.copyProperties(prodFunc, prodGreatDTO);
            mergedList.add(prodGreatDTO);
        }

        Page<ProdGreatDTO> mergedPage = new Page<>(current, SystemConstants.MAX_PAGE_SIZE);
        mergedPage.setRecords(mergedList);
        mergedPage.setTotal(prodPage.getTotal() + prodFuncPage.getTotal());
        return mergedPage;
    }


    @Override
    public void publishGood(ProdGreatDTO prodGreatDTO) {
        if (this.query().eq("name", prodGreatDTO.getName()).count() > 0) throw new SthHasCreatedException(OBJECT_HAS_ALIVE);

        Prod prod = new Prod();
        ProdFunc prodFunc = new ProdFunc();

        BeanUtils.copyProperties(prodGreatDTO, prod);
        BeanUtils.copyProperties(prodGreatDTO, prodFunc);

        // prod.setUserId(UserHolder.getUser().getId());
        // 调试选项
        prod.setUserId(1L);

        this.save(prod);
        prodFuncService.save(prodFunc);
    }


    @Override
    public void deleteGood(String name) {
        Prod prod = this.getOne(Wrappers.<Prod>lambdaQuery().eq(Prod::getName, name));
        if (prod == null) throw new SthNotFoundException(OBJECT_NOT_ALIVE);
        //TODO: 需要判断有无开启交易
        this.removeById(prod.getId());
        prodFuncService.removeById(prod.getId());
    }


    @Override
    @Transactional
    public void updateStatus(ProdLocateDTO prodLocateDTO, Integer func) {

        String name = prodLocateDTO.getName();
        Long userId = prodLocateDTO.getUserId();

        if (name == null || userId == null) throw new BadArgsException(BAD_ARGS);

        Prod prod = this.getOne(new LambdaQueryWrapper<Prod>()
                .eq(Prod::getName, name)
                .eq(Prod::getUserId, userId)
        );

        if (prod == null) throw new SthNotFoundException(OBJECT_NOT_ALIVE);

        ProdFunc prodFunc = prodFuncService.getOne(new LambdaQueryWrapper<ProdFunc>()
                .eq(ProdFunc::getId, prod.getId())
        );

        if (func == 0) {

            prodFunc.setShowoffStatus(0);  //基础功能类型: 无展示提升
            prodFunc.setShowoffEndtime(LocalDateTime.now().plusDays(1)); // 1天

        } else if (func == 1) {

            prodFunc.setShowoffStatus(1); //高级功能类型: 3days展示提升 : 首页提升榜单
            prodFunc.setShowoffEndtime(LocalDateTime.now().plusDays(3)); // 3天

        } else {

            prodFunc.setShowoffStatus(2); //超级功能类型: 7days展示提升 : 首页提升榜单 + 首页轮播图
            prodFunc.setShowoffEndtime(LocalDateTime.now().plusDays(7)); // 7天
        }

        prodFuncService.updateById(prodFunc);

    }

    @Override
    public ProdGreatVO GetByNameSingle(String name) {

        Prod prod = this.getOne(Wrappers.<Prod>lambdaQuery()
                .eq(Prod::getName, name)
//                .eq(Prod::getUserId, UserHolder.getUser().getId()));
                // 调试选项
                .eq(Prod::getUserId, 1L));

        if (prod == null) throw new SthNotFoundException(OBJECT_NOT_ALIVE);


        ProdGreatVO prodGreatVO;

        try {
            prodGreatVO = dtoUtils.createAndCombineDTOs(ProdGreatVO.class, prod.getId(), ProdAllDTO.class, ProdFuncAllDTO.class);
        } catch (Exception e) {
            throw new BaseException(UNKNOWN_ERROR);
        }

        return prodGreatVO;
    }

    @Override
    public Page<Prod> getPageByCate(String cate, Integer current) {

        ProdCate prodCate = prodCateService.getOne(Wrappers.<ProdCate>lambdaQuery()
                .eq(ProdCate::getName, cate));

        if (prodCate == null) throw new SthNotFoundException(OBJECT_NOT_ALIVE);

        Long id = prodCate.getId();

        return this.page(
                new Page<>(current, SystemConstants.MAX_PAGE_SIZE),
                Wrappers.<Prod>lambdaQuery().eq(Prod::getCategoryId, id));
    }

    @Override
    public Page<Prod> pageCateAllProd(String cate, Integer current) {

        ProdCate prodCate = prodCateService.getOne(Wrappers.<ProdCate>lambdaQuery()
                .eq(ProdCate::getName, cate));

        if (prodCate == null) throw new SthNotFoundException(OBJECT_NOT_ALIVE);

        Long id = prodCate.getId();

        return this.page(
                new Page<>(current, SystemConstants.MAX_PAGE_SIZE),
                Wrappers.<Prod>lambdaQuery().eq(Prod::getCategoryId, id));
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
