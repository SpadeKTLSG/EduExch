package com.shop.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.pojo.Result;
import com.shop.pojo.dto.UserGreatDTO;
import com.shop.pojo.vo.UserVO;
import com.shop.serve.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.shop.common.constant.SystemConstant.MAX_PAGE_SIZE;

/**
 * 员工用户控制
 *
 * @author SK
 * @date 2024/06/01
 */
@Slf4j
@Tag(name = "User", description = "用户控制")
@RequestMapping("/admin/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;


    //! Func


    //! ADD
    //管理员手动添加用户: 禁止


    //! DELETE
    //管理员手动删除用户: 禁止


    //! UPDATE

    /**
     * 选择性更新用户信息 包治百病!
     */
    @PutMapping("/update")
    @Operation(summary = "选择性更新用户信息")
    @Parameters(@Parameter(name = "userGreatDTO", description = "User update DTO", required = true))
    public Result update(@RequestBody UserGreatDTO userGreatDTO) {
        try {
            userService.updateUserGreatDTO(userGreatDTO);
            return Result.success();
        } catch (RuntimeException | InstantiationException | IllegalAccessException e) {
            return Result.error(e.getMessage());
        }
    }
    //http://localhost:8085/admin/user/update


    //! QUERY

    /**
     * Account查用户
     */
    @GetMapping("/{account}")
    @Operation(summary = "Account查用户")
    @Parameters(@Parameter(name = "account", description = "用户账号", required = true))
    public Result getByAccount(@PathVariable("account") String account) {
        return Result.success(userService.getByAccount(account));
    }
    //http://localhost:8085/admin/user/cwxtlsg


    /**
     * ID查用户
     */
    @GetMapping("/specify/{id}")
    @Operation(summary = "ID查用户")
    @Parameters(@Parameter(name = "id", description = "用户ID", required = true))
    public Result getByUserId(@PathVariable("id") Long id) {
        return Result.success(userService.getByUserId(id));
    }
    //http://localhost:8085/admin/user/specify/1


    /**
     * 分页查全部用户
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    @Parameters(@Parameter(name = "current", description = "当前页", required = true))
    public Result pageQuery(@RequestParam(value = "current", defaultValue = "1") Integer current) {

        return Result.success(userService.page(new Page<>(current, MAX_PAGE_SIZE)).convert(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }));
    }
    //http://localhost:8085/admin/user/page


    /**
     * Name搜索用户
     */
    //TODO 等前端
}
