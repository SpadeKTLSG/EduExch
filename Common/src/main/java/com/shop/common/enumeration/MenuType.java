package com.shop.common.enumeration;

import lombok.*;

/**
 * 菜单类型
 *
 * @author SK
 */

@NoArgsConstructor
@Getter
public enum MenuType {

    /**
     * 目录
     */
    CATALOG(0),

    /**
     * 菜单
     */
    MENU(1),

    /**
     * 按钮
     */
    BUTTON(2);

    private int value;

    MenuType(int value) {
        this.value = value;
    }

}
