/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.shop.serve.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.pojo.menu.SysMenu;
import com.shop.serve.mapper.SysMenuMapper;
import com.shop.serve.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {


    @Override
    public List<SysMenu> listMenuByUserId(Long userId) {
        // 用户的所有菜单信息
        //直接列出所有菜单
        List<SysMenu> sysMenus = this.query().list();


        Map<Long, List<SysMenu>> sysMenuLevelMap = sysMenus.stream()
                .sorted(Comparator.comparing(SysMenu::getOrderNum))
                .collect(Collectors.groupingBy(SysMenu::getParentId));


        // 一级菜单
        List<SysMenu> rootMenu = sysMenuLevelMap.get(0L);
        if (CollectionUtil.isEmpty(rootMenu)) {
            return Collections.emptyList();
        }

        // 二级菜单
        for (SysMenu sysMenu : rootMenu) {
            sysMenu.setList(sysMenuLevelMap.get(sysMenu.getMenuId()));
        }

        return rootMenu;
    }

    @Override
    public void deleteMenuAndRoleMenu(Long menuId) {
        //删除菜单
        this.removeById(menuId);
    }

    @Override
    public List<SysMenu> listSimpleMenuNoButton() {

        return this.baseMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                .ne(SysMenu::getType, 2)
                .orderByAsc(SysMenu::getOrderNum));

    }

    @Override
    public List<SysMenu> listRootMenu() {

        return this.baseMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getType, 0)
                .orderByAsc(SysMenu::getOrderNum));
    }

    @Override
    public List<SysMenu> listChildrenMenuByParentId(Long parentId) {

        return this.baseMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, parentId)
                .select(SysMenu::getMenuId, SysMenu::getName));
    }

    @Override
    public List<SysMenu> listMenuAndBtn() {

        return this.baseMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                .orderByAsc(SysMenu::getOrderNum));
    }

}
