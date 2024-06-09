package com.shop.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品分类 DTO
 *
 * @author SK
 * @date 2024/06/02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdCateAllDTO {

    /**
     * 分类名
     */
    private String name;

    /**
     * 描述
     */
    private String description;

}
