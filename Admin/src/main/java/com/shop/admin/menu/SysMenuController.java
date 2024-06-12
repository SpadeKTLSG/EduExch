package com.shop.admin.menu;

import cn.hutool.core.util.StrUtil;
import com.shop.common.enumeration.MenuType;
import com.shop.common.exception.BaseException;
import com.shop.pojo.menu.SysMenu;
import com.shop.pojo.res.Result;
import com.shop.serve.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.shop.common.constant.SystemConstant.SYS_MENU_MAX_ID;

/**
 * 系统菜单
 *
 * @author mall4j - Front
 */
@Slf4j
@Tag(name = "SysMenuController", description = "系统菜单")
@RequestMapping("/sys/menu")
@RestController
public class SysMenuController {


    @Autowired
    private SysMenuService sysMenuService;

    //! 警告, 这里是正在建设的前端接口, 请勿直接使用, 本页面将会处于非常不稳定的状态

    @GetMapping("/nav")
    @Operation(summary = "获取用户所拥有的菜单和权限", description = "通过登陆用户的userId获取用户所拥有的菜单和权限")
    public Result nav() {
        List<SysMenu> menuList = sysMenuService.listMenuByUserId(1L); //调试选项
        return Result.success(menuList);
    }
    //http://localhost:8085/sys/menu/nav

    /**
     * 获取菜单页面的表
     */
    @GetMapping("/table")
    @Operation(summary = "获取菜单页面的表", description = "获取菜单页面的表")
    public Result table() {
        List<SysMenu> sysMenuList = sysMenuService.listMenuAndBtn();
        return Result.success(sysMenuList);
    }

    /**
     * 所有菜单列表(用于新建、修改角色时 获取菜单的信息)
     */
    @GetMapping("/list")
    @Operation(summary = "获取用户所拥有的菜单(不包括按钮)", description = "通过登陆用户的userId获取用户所拥有的菜单和权限")
    public Result list() {
        List<SysMenu> sysMenuList = sysMenuService.listSimpleMenuNoButton();
        return Result.success(sysMenuList);
    }

    /**
     * 选择菜单
     */
    @GetMapping("/listRootMenu")
    public Result listRootMenu() {
        //查询列表数据
        List<SysMenu> menuList = sysMenuService.listRootMenu();

        return Result.success(menuList);
    }

    /**
     * 选择子菜单
     */
    @GetMapping("/listChildrenMenu")
    @Operation(summary = "选择子菜单", description = "选择子菜单")
    public Result listChildrenMenu(Long parentId) {
        //查询列表数据
        List<SysMenu> menuList = sysMenuService.listChildrenMenuByParentId(parentId);

        return Result.success(menuList);
    }

    /**
     * 菜单信息
     */
    @GetMapping("/info/{menuId}")
    @Operation(summary = "菜单信息", description = "菜单信息")
    public Result info(@PathVariable("menuId") Long menuId) {
        SysMenu menu = sysMenuService.getById(menuId);
        return Result.success(menu);
    }

    /**
     * 保存
     */
    @PostMapping
    @Operation(summary = "保存菜单", description = "保存菜单")
    public Result save(@Valid @RequestBody SysMenu menu) {
        //数据校验
        verifyForm(menu);
        sysMenuService.save(menu);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @Operation(summary = "修改菜单", description = "修改菜单")
    public Result update(@Valid @RequestBody SysMenu menu) {
        //数据校验
        verifyForm(menu);

        if (menu.getType() == MenuType.MENU.getValue()) {
            if (StrUtil.isBlank(menu.getUrl())) {
                return Result.error("菜单URL不能为空");
            }
        }
        sysMenuService.updateById(menu);

        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{menuId}")
    @Operation(summary = "删除菜单", description = "删除菜单")
    public Result delete(@PathVariable Long menuId) {

        if (menuId <= SYS_MENU_MAX_ID) {
            return Result.error("系统菜单，不能删除");
        }

        //判断是否有子菜单或按钮
        List<SysMenu> menuList = sysMenuService.listChildrenMenuByParentId(menuId);

        if (!menuList.isEmpty()) {
            return Result.error("请先删除子菜单或按钮");
        }

        sysMenuService.deleteMenuAndRoleMenu(menuId);

        return Result.success();
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(SysMenu menu) {

        if (menu.getType() == MenuType.MENU.getValue()) {
            if (StrUtil.isBlank(menu.getUrl())) {
                throw new BaseException("菜单URL不能为空");
            }
        }
        if (Objects.equals(menu.getMenuId(), menu.getParentId())) {
            throw new BaseException("自己不能是自己的上级");
        }

        //上级菜单类型
        int parentType = MenuType.CATALOG.getValue();
        if (menu.getParentId() != 0) {
            SysMenu parentMenu = sysMenuService.getById(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == MenuType.CATALOG.getValue() ||
                menu.getType() == MenuType.MENU.getValue()) {
            if (parentType != MenuType.CATALOG.getValue()) {
                throw new BaseException("上级菜单只能为目录类型");
            }
            return;
        }

        //按钮
        if (menu.getType() == MenuType.BUTTON.getValue()) {
            if (parentType != MenuType.MENU.getValue()) {
                throw new BaseException("上级菜单只能为菜单类型");
            }
        }
    }
}
