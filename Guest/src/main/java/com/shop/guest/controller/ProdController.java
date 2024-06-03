package com.shop.guest.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.common.utils.SystemConstants;
import com.shop.pojo.Result;
import com.shop.pojo.dto.ProdAllDTO;
import com.shop.pojo.dto.ProdFuncAllDTO;
import com.shop.pojo.dto.ProdGreatDTO;
import com.shop.pojo.entity.Prod;
import com.shop.pojo.entity.ProdCate;
import com.shop.pojo.entity.ProdFunc;
import com.shop.pojo.vo.ProdGreatVO;
import com.shop.serve.service.ProdCateService;
import com.shop.serve.service.ProdFuncService;
import com.shop.serve.service.ProdService;
import com.shop.serve.tool.NewDTOUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Prod", description = "商品")
@RequestMapping("/guest/prod")
@RestController
public class ProdController {

    @Autowired
    private ProdService prodService;
    @Autowired
    private ProdFuncService prodFuncService;
    @Autowired
    private ProdCateService prodCateService;

    @Autowired
    private NewDTOUtils dtoUtils;


    //! Func


    //! ADD

    /**
     * 用户添加商品
     * <p>需要审核修改status</p>
     */
    @PostMapping("/save")
    @Operation(summary = "用户添加商品")
    @Parameters(@Parameter(name = "prodGreatDTO", description = "商品添加DTO", required = true))
    public Result save(@RequestBody ProdGreatDTO prodGreatDTO) {

        // 需要拆分为两个实体 : prod 和 prodFunc

        if (prodService.query().eq("name", prodGreatDTO.getName()).count() > 0) {
            return Result.success("商品已存在");
        }

        Prod prod = new Prod();
        ProdFunc prodFunc = new ProdFunc();

        BeanUtils.copyProperties(prodGreatDTO, prod);
        BeanUtils.copyProperties(prodGreatDTO, prodFunc);

        // prod.setUserId(UserHolder.getUser().getId());
        // 调试选项
        prod.setUserId(1L);

        prodService.save(prod);
        prodFuncService.save(prodFunc);

        return Result.success();
    }
    //http://localhost:8086/guest/prod/save


    //! DELETE

    /**
     * 用户删除商品, 通过商品名
     * <p>需要判断有无开启交易</p>
     */
    @DeleteMapping("/delete/{name}")
    @Operation(summary = "用户删除商品")
    @Parameters(@Parameter(name = "name", description = "商品名", required = true))
    public Result delete(@PathVariable("name") String name) {
        Prod prod = prodService.getOne(Wrappers.<Prod>lambdaQuery().eq(Prod::getName, name));
        if (prod == null) {
            return Result.error("商品不存在");
        }
        //TODO: 需要判断有无开启交易
        prodService.removeById(prod.getId());
        prodFuncService.removeById(prod.getId());
        return Result.success();
    }
    //http://localhost:8086/guest/prod/delete


    //! UPDATE

    /**
     * 用户更新商品 联表选择性更新字段
     * <p>包括: 商品冻结/恢复</p>
     */
    @PutMapping("/update")
    @Operation(summary = "用户更新商品")
    @Parameters(@Parameter(name = "prodGreatDTO", description = "商品更新DTO", required = true))
    public Result update(@RequestBody ProdGreatDTO prodGreatDTO) throws InstantiationException, IllegalAccessException {

        try {
            prodService.updateUserGreatDTO(prodGreatDTO);
            return Result.success();
        } catch (RuntimeException | InstantiationException | IllegalAccessException e) {
            return Result.error(e.getMessage());
        }
    }
    //http://localhost:8086/guest/prod/update


    //! QUERY

    /**
     * 分页查询商品分类列表
     * <p>用于前端填表单</p>
     */
    @GetMapping("/category/page")
    @Operation(summary = "分页查询商品分类列表")
    @Parameters(@Parameter(name = "current", description = "当前页", required = true))
    public Result pageCateQuery(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return Result.success(prodCateService.page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE)));
    }
    //http://localhost:8086/guest/prod/category/page


    /**
     * 分页查询自己的商品列表
     * <p>简单展示VO</p>
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询自己的商品列表")
    @Parameters(@Parameter(name = "current", description = "当前页", required = true))
    public Result pageProdQuery(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return Result.success(prodService.page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE),
                        Wrappers.<Prod>lambdaQuery()
//                        .eq(Prod::getUserId, UserHolder.getUser().getId()))
                                // 调试选项
                                .eq(Prod::getUserId, 1L))
        );
    }
    //http://localhost:8086/guest/prod/page


    /**
     * name查询单个商品详细信息
     * <p>联表查询VO</p>
     */
    @GetMapping("/{name}")
    @Operation(summary = "查询单个商品详细信息")
    @Parameters(@Parameter(name = "name", description = "商品名", required = true))
    public Result getByName(@PathVariable("name") String name) {

        Prod prod = prodService.getOne(Wrappers.<Prod>lambdaQuery()
                .eq(Prod::getName, name)
//                .eq(Prod::getUserId, UserHolder.getUser().getId()));
                // 调试选项
                .eq(Prod::getUserId, 1L));

        if (prod == null) {
            return Result.error("商品不存在");
        }


        ProdGreatVO prodGreatVO;
        try {
            prodGreatVO = dtoUtils.createAndCombineDTOs(ProdGreatVO.class, prod.getId(), ProdAllDTO.class, ProdFuncAllDTO.class);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

        return Result.success(prodGreatVO);
    }
    //http://localhost:8086/guest/prod/


    /**
     * 根据分类查自己的商品列表分页(半联表)
     */
    @GetMapping("/category/prod/{cate}")
    @Operation(summary = "根据分类获得自己的对应商品列表")
    @Parameters(@Parameter(name = "cate", description = "分类名", required = true))
    public Result getPageByCate(@PathVariable("cate") String cate, @RequestParam(value = "current", defaultValue = "1") Integer current) {

        ProdCate prodCate = prodCateService.getOne(Wrappers.<ProdCate>lambdaQuery()
                .eq(ProdCate::getName, cate));

        if (prodCate == null) {
            return Result.error("该分类不存在");
        }

        Long id = prodCate.getId();

        return Result.success(prodService.page(
                new Page<>(current, SystemConstants.MAX_PAGE_SIZE),
                Wrappers.<Prod>lambdaQuery().eq(Prod::getCategoryId, id)));

    }
    //http://localhost:8086/guest/prod/category/prod/0


}
