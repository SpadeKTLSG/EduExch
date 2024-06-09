package com.shop.serve.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.constant.SystemConstant;
import com.shop.common.context.UserHolder;
import com.shop.common.exception.*;
import com.shop.common.utils.CacheClient;
import com.shop.pojo.dto.*;
import com.shop.pojo.entity.Order;
import com.shop.pojo.entity.Prod;
import com.shop.pojo.entity.ProdCate;
import com.shop.pojo.entity.ProdFunc;
import com.shop.pojo.vo.ProdGreatVO;
import com.shop.serve.mapper.ProdMapper;
import com.shop.serve.service.*;
import com.shop.serve.tool.NewDTOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.shop.common.constant.MessageConstant.*;
import static com.shop.common.constant.RedisConstant.*;
import static com.shop.common.constant.SystemConstant.DEFAULT_WEIGHT;
import static com.shop.common.utils.NewBeanUtil.dtoMapService;

@Slf4j
@Service
public class ProdServiceImpl extends ServiceImpl<ProdMapper, Prod> implements ProdService {

    @Autowired
    private ProdFuncService prodFuncService;
    @Autowired
    private ProdCateService prodCateService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private UpshowService upshowService;
    @Autowired
    private RotationService rotationService;
    @Autowired
    private HotsearchService hotsearchService;

    @Autowired
    private NewDTOUtils dtoUtils;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CacheClient cacheClient;


    @Override
    @Transactional
    public void update4User(ProdGreatDTO prodGreatDTO) throws InstantiationException, IllegalAccessException {
        // 联表选择性更新
        Optional<Prod> optionalProd = Optional.ofNullable(this.getOne(Wrappers.<Prod>lambdaQuery().eq(Prod::getName, prodGreatDTO.getName())));
        if (optionalProd.isEmpty()) throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);

        optionalProd.get().setUserId(UserHolder.getUser().getId());//这里商品的userId是自己

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

        prodFunc.setStatus(ProdFunc.NORMAL);
        prodFuncService.updateById(prodFunc);
    }


    @Override
    public void freeze(ProdLocateDTO prodLocateDTO) {

        if (this.query().eq("name", prodLocateDTO.getName()).count() == 0) throw new SthNotFoundException(OBJECT_NOT_ALIVE);

        if (this.query().eq("user_id", prodLocateDTO.getUserId()).count() == 0) throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);


        Prod prod = this.getOne(new LambdaQueryWrapper<Prod>()// 找到对应商品id, 通过id找到另一张表UserFunc, 修改状态字段
                .eq(Prod::getName, prodLocateDTO.getName())
                .eq(Prod::getUserId, prodLocateDTO.getUserId())
        );

        ProdFunc prodFunc = prodFuncService.getOne(new LambdaQueryWrapper<ProdFunc>()
                .eq(ProdFunc::getId, prod.getId())
        );

        prodFunc.setStatus(ProdFunc.FROZEN);
        prodFuncService.updateById(prodFunc);

        //同时需要将商品从首页提升榜单和首页轮播图中移除
        if (!Objects.equals(prodFunc.getShowoffStatus(), ProdFunc.BASIC)) {
            if (Objects.equals(prodFunc.getShowoffStatus(), ProdFunc.SENIOR)) {
                upshowService.remove4Upshow(prodLocateDTO);
            } else {
                upshowService.remove4Upshow(prodLocateDTO);
                rotationService.remove4Rotation(prodLocateDTO);
            }
        }

    }


    @Override
    public Page<ProdGreatDTO> page2Check(Integer current) {
        Page<ProdFunc> prodFuncPage = prodFuncService.page(new Page<>(current, SystemConstant.MAX_PAGE_SIZE),
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

        Page<ProdGreatDTO> mergedPage = new Page<>(current, SystemConstant.MAX_PAGE_SIZE);
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

        Page<Prod> prodPage = this.page(new Page<>(current, SystemConstant.MAX_PAGE_SIZE));
        Page<ProdFunc> prodFuncPage = prodFuncService.page(new Page<>(current, SystemConstant.MAX_PAGE_SIZE));
        List<ProdGreatDTO> mergedList = new ArrayList<>(); // 存储合并后的结果

        for (int i = 0; i < prodPage.getRecords().size(); i++) {
            Prod prod = prodPage.getRecords().get(i);
            ProdFunc prodFunc = prodFuncPage.getRecords().get(i);

            ProdGreatDTO prodGreatDTO = new ProdGreatDTO();
            BeanUtils.copyProperties(prod, prodGreatDTO);
            BeanUtils.copyProperties(prodFunc, prodGreatDTO);
            mergedList.add(prodGreatDTO);
        }

        Page<ProdGreatDTO> mergedPage = new Page<>(current, SystemConstant.MAX_PAGE_SIZE);
        mergedPage.setRecords(mergedList);
        mergedPage.setTotal(prodPage.getTotal() + prodFuncPage.getTotal());
        return mergedPage;
    }


    @Override
    @Transactional
    public void publishGood(ProdGreatDTO prodGreatDTO) {
        if (this.query().eq("name", prodGreatDTO.getName()).count() > 0) throw new SthHasCreatedException(OBJECT_HAS_ALIVE);

        Prod prod = new Prod();
        ProdFunc prodFunc = new ProdFunc();

        BeanUtils.copyProperties(prodGreatDTO, prod);
        BeanUtils.copyProperties(prodGreatDTO, prodFunc);

        prod.setUserId(UserHolder.getUser().getId());

        this.save(prod);
        prodFuncService.save(prodFunc);

        //还需要添加Redis Key
        stringRedisTemplate.opsForValue().set(SECKILL_STOCK_KEY + prod.getId(), prod.getStock().toString());
    }


    @Override
    @Transactional
    public void deleteGood(String name) {
        Prod prod = this.getOne(Wrappers.<Prod>lambdaQuery().eq(Prod::getName, name));
        if (prod == null) throw new SthNotFoundException(OBJECT_NOT_ALIVE);

        //需要判断是否有已经开启的交易
        Order order = orderService.getOne(Wrappers.<Order>lambdaQuery()
                .eq(Order::getProdId, prod.getId())
                .ne(Order::getStatus, Order.OVER) //已经完成的交易不算
                .ne(Order::getStatus, Order.STOP) //已经撤销的交易不算
        );

        if (order != null) throw new SthHasCreatedException(OBJECT_HAS_ALIVE);

        prodFuncService.removeById(prod.getId());
        this.removeById(prod.getId());

        //还需要删除Redis Key
        stringRedisTemplate.delete(SECKILL_STOCK_KEY + prod.getId());
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

            prodFunc.setShowoffStatus(1);  //基础功能类型: 无展示提升
            prodFunc.setShowoffEndtime(LocalDateTime.now().plusDays(1)); // 1天

        } else if (func == 1) {

            prodFunc.setShowoffStatus(1); //高级功能类型: 3days展示提升 : 首页提升榜单
            prodFunc.setShowoffEndtime(LocalDateTime.now().plusDays(3)); // 3天

            UpshowDTO upshowDTO = UpshowDTO.builder()
                    .prodId(prod.getId())
                    .name(prod.getName())
                    .build();
            upshowService.add2Upshow(upshowDTO);

        } else {

            prodFunc.setShowoffStatus(2); //超级功能类型: 7days展示提升 : 首页提升榜单 + 首页轮播图
            prodFunc.setShowoffEndtime(LocalDateTime.now().plusDays(7)); // 7天

            UpshowDTO upshowDTO = UpshowDTO.builder()
                    .prodId(prod.getId())
                    .name(prod.getName())
                    .build();
            upshowService.add2Upshow(upshowDTO);

            RotationDTO rotationDTO = RotationDTO.builder()
                    .prodId(prod.getId())
                    .name(prod.getName())
                    .picture(prod.getImages())
                    .weight(prodFunc.getWeight())
                    .build();
            rotationService.add2Rotation(rotationDTO);
        }


        prodFuncService.updateById(prodFunc);

    }

    @Override
    @Transactional
    public ProdGreatVO GetByNameSingle(ProdLocateDTO prodLocateDTO) {

        Prod prod = this.getOne(Wrappers.<Prod>lambdaQuery()
                .eq(Prod::getName, prodLocateDTO.getName())
                .eq(Prod::getUserId, prodLocateDTO.getUserId()));


        if (prod == null) throw new SthNotFoundException(OBJECT_NOT_ALIVE);

        //视为一次对具体商品的浏览, 记录浏览量到Redis
        String productKey = USER_VO_KEY + prod.getId();

        // 使用HyperLogLog记录用户id -> 浏览商品记录
        stringRedisTemplate.opsForHyperLogLog().add(productKey, UserHolder.getUser().getId().toString());

        // 统计该商品浏览量
        Long count = stringRedisTemplate.opsForHyperLogLog().size(productKey);

        // 更新商品浏览量, 同时提升权重
        ProdFunc prodFunc = prodFuncService.getOne(Wrappers.<ProdFunc>lambdaQuery().eq(ProdFunc::getId, prod.getId()));
        prodFunc.setVisit(prodFunc.getVisit() + count);
        prodFunc.setWeight(prodFunc.getWeight() + count * DEFAULT_WEIGHT);
        prodFuncService.updateById(prodFunc);

        ProdGreatVO prodGreatVO;

        try {
            prodGreatVO = dtoUtils.createAndCombineDTOs(ProdGreatVO.class, prod.getId(), ProdAllDTO.class, ProdFuncAllDTO.class);
        } catch (Exception e) {
            throw new BaseException(UNKNOWN_ERROR);
        }

        return prodGreatVO;
    }

    /**
     * 通过缓存穿透等方法查询商品
     */
    @Override
    public ProdGreatVO GetByNameSingleCache(ProdLocateDTO prodLocateDTO) {

        // 这里不能用prodLocateDTO直接查, 需要用prodLocateDTO的name和userId拼接,
        // 因为这里不允许传递id进来查询, 于是参照CacheClient的缓存穿透查询进行改造, 存在Prod内作为内部方法

        Prod prod = queryProdWithPassThrough(prodLocateDTO);

        //? 以下为通用余下流程

        if (prod == null) throw new SthNotFoundException(OBJECT_NOT_ALIVE);

        //视为一次对具体商品的浏览, 记录浏览量到Redis
        String productKey = USER_VO_KEY + prod.getId();

        // 使用HyperLogLog记录用户id -> 浏览商品记录
        stringRedisTemplate.opsForHyperLogLog().add(productKey, UserHolder.getUser().getId().toString());

        // 统计该商品浏览量
        Long count = stringRedisTemplate.opsForHyperLogLog().size(productKey);

        // 更新商品浏览量, 同时提升权重
        ProdFunc prodFunc = prodFuncService.getOne(Wrappers.<ProdFunc>lambdaQuery().eq(ProdFunc::getId, prod.getId()));
        prodFunc.setVisit(prodFunc.getVisit() + count);
        prodFunc.setWeight(prodFunc.getWeight() + count * DEFAULT_WEIGHT);
        prodFuncService.updateById(prodFunc);

        ProdGreatVO prodGreatVO;

        try {
            prodGreatVO = dtoUtils.createAndCombineDTOs(ProdGreatVO.class, prod.getId(), ProdAllDTO.class, ProdFuncAllDTO.class);
        } catch (Exception e) {
            throw new BaseException(UNKNOWN_ERROR);
        }

        return prodGreatVO;
    }


    /**
     * 通过缓存穿透方法查询商品
     */
    private Prod queryProdWithPassThrough(ProdLocateDTO prodLocateDTO) {
        //针对传入的prodLocateDTO进行Key构建: 选择name和userId作为Key, userId.toString() : name, 加上前缀
        String key = CACHE_PROD_KEY + prodLocateDTO.getUserId() + ":" + prodLocateDTO.getName();

        String prodJson = stringRedisTemplate.opsForValue().get(key);//执行查询

        if (StrUtil.isNotBlank(prodJson)) { //不为空就把json转为Prod返回, 表示拿到了prod
            return JSONUtil.toBean(prodJson, Prod.class);
        }

        if (prodJson != null) { //查询到了, 但是是空字符串, 意味着是缓存空数据, 返回null
            return null;
        }

        //查数据库流程: MP的lambdaQuery查询
        Prod prod = this.getOne(Wrappers.<Prod>lambdaQuery()
                .eq(Prod::getName, prodLocateDTO.getName())
                .eq(Prod::getUserId, prodLocateDTO.getUserId()));


        if (prod == null) { //还查不到就要进行缓存穿透的空数据设置
            stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES); //设置TTL - NULL
            return null;
        }

        //查到了就要进行缓存设置
        String jsonStr = JSONUtil.toJsonStr(prod);
        stringRedisTemplate.opsForValue().set(key, jsonStr, CACHE_PROD_TTL, TimeUnit.MINUTES); //设置TTL - PROD

        return prod;
    }


    @Override
    public Page<Prod> getPageByCate(String cate, Integer current) {

        ProdCate prodCate = prodCateService.getOne(Wrappers.<ProdCate>lambdaQuery()
                .eq(ProdCate::getName, cate));

        if (prodCate == null) throw new SthNotFoundException(OBJECT_NOT_ALIVE);

        Long id = prodCate.getId();

        return this.page(
                new Page<>(current, SystemConstant.MAX_PAGE_SIZE),
                Wrappers.<Prod>lambdaQuery().eq(Prod::getCategoryId, id));
    }


    @Override
    public Page<Prod> pageCateAllProd(String cate, Integer current) {

        ProdCate prodCate = prodCateService.getOne(Wrappers.<ProdCate>lambdaQuery()
                .eq(ProdCate::getName, cate));

        if (prodCate == null) throw new SthNotFoundException(OBJECT_NOT_ALIVE);

        Long id = prodCate.getId();

        return this.page(
                new Page<>(current, SystemConstant.MAX_PAGE_SIZE),
                Wrappers.<Prod>lambdaQuery().eq(Prod::getCategoryId, id));
    }


    @Override
    public List<ProdFunc> getOutdateOnes(LocalDateTime time) {
        List<ProdFunc> prodList2Check = prodFuncService.query() //需要保证其ShowoffEndtime存在!
                .isNotNull("showoff_endtime")
                .list();

        //需要手动取出来判断是否过期
        prodList2Check.removeIf(prodFunc -> prodFunc.getShowoffEndtime().isAfter(time));

        return prodList2Check;
    }


    @Override
    public void cooldownUpshowProd(ProdFunc prodFunc) {
        prodFunc.setShowoffStatus(0);
        prodFunc.setShowoffEndtime(LocalDateTime.now()); //只能设置为现在而不是null否则报错

        UpshowDTO upshowDTO = UpshowDTO.builder()
                .prodId(prodFunc.getId())
                .name(this.query().eq("id", prodFunc.getId()).one().getName())
                .build();

        prodFuncService.updateById(prodFunc);
        upshowService.remove4Upshow(upshowDTO);
    }


    @Override
    public void cooldownRotationProd(ProdFunc prodFunc) {
        prodFunc.setShowoffStatus(0);
        prodFunc.setShowoffEndtime(LocalDateTime.now()); //只能设置为现在而不是null否则报错

        RotationDTO rotationDTO = RotationDTO.builder()
                .prodId(prodFunc.getId())
                .name(this.query().eq("id", prodFunc.getId()).one().getName())
                .build();

        prodFuncService.updateById(prodFunc);
        rotationService.remove4Rotation(rotationDTO);
    }


    @Override
    public List<ProdFunc> extractList4HotProd() {

        List<ProdFunc> prodList2Check = prodFuncService
                .query()
                .orderByDesc("visit")
                .last("limit " + SystemConstant.MAX_PAGE_SIZE)
                .list();

        return prodList2Check;
    }


    @Override
    public void add2HotSearch(ProdFunc prodFunc) {
        HotsearchDTO hotsearchDTO = HotsearchDTO.builder()
                .visit(prodFunc.getVisit())
                .prodId(prodFunc.getId())
                .name(this.query().eq("id", prodFunc.getId()).one().getName())
                .build();

        hotsearchService.add2Hotsearch(hotsearchDTO);
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
