package com.shop.guest.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.common.constant.SystemConstants;
import com.shop.common.context.UserHolder;
import com.shop.pojo.Result;
import com.shop.pojo.dto.ProdGreatDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Prod;
import com.shop.serve.service.ProdCateService;
import com.shop.serve.service.ProdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品
 *
 * @author SK
 * @date 2024/06/03
 */
@Slf4j
@Tag(name = "Prod", description = "商品")
@RequestMapping("/guest/prod")
@RestController
public class ProdController {

    @Autowired
    private ProdService prodService;
    @Autowired
    private ProdCateService prodCateService;


    //! Func


    //! ADD

    /**
     * 用户添加商品
     * <p>需要审核修改status</p>
     */
    @PostMapping("/save")
    @Operation(summary = "用户添加商品")
    @Parameters(@Parameter(name = "prodGreatDTO", description = "商品添加DTO", required = true))
    public Result publishGood(@RequestBody ProdGreatDTO prodGreatDTO) {
        prodService.publishGood(prodGreatDTO);
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
    public Result deleteGood(@PathVariable("name") String name) {
        prodService.deleteGood(name);
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
    public Result update4User(@RequestBody ProdGreatDTO prodGreatDTO) throws InstantiationException, IllegalAccessException {

        try {
            prodService.update4User(prodGreatDTO);
            return Result.success();
        } catch (RuntimeException | InstantiationException | IllegalAccessException e) {
            return Result.error(e.getMessage());
        }
    }
    //http://localhost:8086/guest/prod/update


    /**
     * 优惠券触发商品状态修改
     */
    @PutMapping("/update/status/{func}")
    @Operation(summary = "优惠券触发商品状态修改")
    @Parameters({
            @Parameter(name = "prodLocateDTO", description = "商品定位DTO", required = true),
            @Parameter(name = "func", description = "功能", required = true)
    })
    public Result updateStatus(@RequestBody ProdLocateDTO prodLocateDTO, @PathVariable("func") Integer func) {
        prodService.updateStatus(prodLocateDTO, func);

        return Result.success();
    }

    //! QUERY

    /**
     * 分页查询商品 分类 列表
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
                        .eq(Prod::getUserId, UserHolder.getUser().getId()))
        );
    }
    //http://localhost:8086/guest/prod/page


    /**
     * name查询自己单个商品详细信息
     * <p>联表查询VO</p>
     */
    @GetMapping("/get")
    @Operation(summary = "查询单个商品详细信息")
    @Parameters(@Parameter(name = "prodLocateDTO", description = "商品定位DTO"))
    public Result getProd(@RequestBody ProdLocateDTO prodLocateDTO) {
        return Result.success(prodService.GetByNameSingle(prodLocateDTO));
    }
    //http://localhost:8086/guest/prod/get


    /**
     * 根据分类查自己的商品列表分页(半联表)
     */
    @GetMapping("/category/prod/{cate}")
    @Operation(summary = "根据分类获得自己的对应商品列表")
    @Parameters(@Parameter(name = "cate", description = "分类名", required = true))
    public Result getPageByCate(@PathVariable("cate") String cate, @RequestParam(value = "current", defaultValue = "1") Integer current) {
        return Result.success(prodService.getPageByCate(cate, current));
    }
    //http://localhost:8086/guest/prod/category/prod/0


    /**
     * 分页查询所有商品列表(仅Prod表)
     */
    @GetMapping("/all/page")
    @Operation(summary = "分页查询所有商品列表")
    @Parameters(@Parameter(name = "current", description = "当前页", required = true))
    public Result pageAllProd(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return Result.success(prodService.page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE)));
    }
    //http://localhost:8086/guest/prod/all/page


    /**
     * 分页查询一个分类下的所有商品列表(联表Prod + ProdCate)
     * <p>用于前端用户浏览</p>
     */
    @GetMapping("/cateall/page/{cate}")
    @Operation(summary = "分页查询一个分类下的所有商品列表")
    @Parameters(@Parameter(name = "cate", description = "分类名", required = true))
    public Result pageCateAllProd(@PathVariable("cate") String cate, @RequestParam(value = "current", defaultValue = "1") Integer current) {
        return Result.success(prodService.pageCateAllProd(cate, current));
    }
    //http://localhost:8086/guest/prod/cateall/page/人类
}
