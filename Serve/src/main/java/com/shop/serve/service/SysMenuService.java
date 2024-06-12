/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.shop.serve.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.menu.SysMenu;

import java.util.List;


public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获取用户菜单列表
     */
    List<SysMenu> listMenuByUserId(Long userId);


    /**
     * 删除菜单
     */
    void deleteMenuAndRoleMenu(Long menuId);


    /**
     * 获取简单的menu tree 用于在ele-ui tree中显示，根据orderNum排序
     */
    List<SysMenu> listSimpleMenuNoButton();


    /**
     * 获取一级菜单
     */
    List<SysMenu> listRootMenu();

    /**
     * 根据一级菜单id 获取二级菜单
     */
    List<SysMenu> listChildrenMenuByParentId(Long parentId);


    /**
     * 获取菜单和按钮列表
     */
    List<SysMenu> listMenuAndBtn();
}
