package com.shop.admin.controller;

import com.shop.pojo.Result;
import com.shop.pojo.dto.ProdCateDTO;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        prodCateService.save(ProdCate.builder()
                .name(prodCateDTO.getName())
                .description(prodCateDTO.getDescription())
                .build());

        return Result.success();
    }
    //http://localhost:8085/admin/prod/cate/save

    //! DELETE


    //! UPDATE


    //! QUERY


}
