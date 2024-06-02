package com.shop.admin.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Prod", description = "商品")
@RequestMapping("/admin/prod")
@RestController
public class ProdController {
}
