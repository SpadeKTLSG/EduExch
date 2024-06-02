package com.shop.guest.controller;

import com.shop.pojo.Result;
import com.shop.pojo.dto.ProdGreatDTO;
import com.shop.pojo.entity.Prod;
import com.shop.pojo.entity.ProdFunc;
import com.shop.serve.service.ProdFuncService;
import com.shop.serve.service.ProdService;
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


    //! Func


    //! ADD

    /**
     * 用户添加商品
     * <p>需要审核修改status</p>
     */
    @PostMapping("/save")
    public Result save(@RequestBody ProdGreatDTO prodGreatDTO) {

        // 需要拆分为两个实体 : prod 和 prodFunc

        if (prodService.query().eq("name", prodGreatDTO.getName()).count() > 0) {
            return Result.success("商品已存在");
        }

        Prod prod = new Prod();
        ProdFunc prodFunc = new ProdFunc();

        BeanUtils.copyProperties(prodGreatDTO, prod);
        BeanUtils.copyProperties(prodGreatDTO, prodFunc);

        prodService.save(prod);
        prodFuncService.save(prodFunc);

        return null;
    }
    //http://localhost:8086/guest/prod/save


    //! DELETE


    //! UPDATE


    //! QUERY

    /**
     * 查询商品分类列表
     * <p>用于前端填表单</p>
     */
    @GetMapping("/category/list")
    public Result categoryList() {
        return null;
    }


}
