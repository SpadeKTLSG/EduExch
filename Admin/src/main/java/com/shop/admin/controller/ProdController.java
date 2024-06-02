package com.shop.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.pojo.Result;
import com.shop.pojo.dto.ProdCateDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Prod;
import com.shop.pojo.entity.ProdCate;
import com.shop.serve.service.ProdCateService;
import com.shop.serve.service.ProdFuncService;
import com.shop.serve.service.ProdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Prod", description = "商品")
@RequestMapping("/admin/prod")
@RestController
public class ProdController {

    @Autowired
    private ProdService prodService;
    @Autowired
    private ProdFuncService prodFuncService;
    @Autowired
    private ProdCateService prodCateService;


    //! Func


    //! ADD

    /**
     * 添加商品分类
     */
    @PostMapping("/cate/save")
    @Operation(summary = "添加商品分类")
    @Parameters(@Parameter(name = "prodCateDTO", description = "商品分类DTO", required = true))
    public Result saveCate(@RequestBody ProdCateDTO prodCateDTO) {

        return (prodCateService.save(ProdCate.builder()
                .name(prodCateDTO.getName())
                .description(prodCateDTO.getDescription())
                .build())) ? Result.success() : Result.error("商品分类已存在");
    }
    //http://localhost:8085/admin/prod/cate/save


    //! DELETE

    /**
     * 管理员删除一件商品 -> name + userId 确定唯一商品
     * <p>联表删除</p>
     */
    @DeleteMapping("/delete/one")
    @Operation(summary = "管理员删除一件商品")
    @Parameters(@Parameter(name = "prodLocateDTO", description = "商品定位DTO", required = true))
    public Result deleteByNameUser(@RequestBody ProdLocateDTO prodLocateDTO) {

        if (prodService.query().eq("name", prodLocateDTO.getName()).count() == 0) {
            return Result.error("商品不存在");
        }

        if (prodService.query().eq("user_id", prodLocateDTO.getUserId()).count() == 0) {
            return Result.error("用户不存在");
        }

        return (prodService.remove(new LambdaQueryWrapper<Prod>()
                .eq(Prod::getName, prodLocateDTO.getName())
                .eq(Prod::getUserId, prodLocateDTO.getUserId())
        )) ? Result.success() : Result.error("商品不存在");
    }
    //http://localhost:8085/admin/prod/delete/one


    //! UPDATE
    // 管理员修改商品信息 : 不允许


    //! QUERY

    /**
     * 分页查询所有商品分类
     */


    /**
     * 分页查询所有商品列表
     */


}
