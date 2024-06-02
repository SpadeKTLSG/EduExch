package com.shop.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品定位DTO
 *
 * @author SK
 * @date 2024/06/02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdLocateDTO {

    /**
     * 名称
     */
    private String name;

    /**
     * 对应用户的ID
     */
    private Long userId;

}
