package com.shop.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 热搜DTO
 *
 * @author SK
 * @date 2024/06/09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotsearchAllDTO {


    /**
     * 对应商品ID
     */
    private Long prodId;

    /**
     * 对应商品名
     */
    private String name;

    /**
     * 对应商品浏览量
     */
    private Long visit;
}
