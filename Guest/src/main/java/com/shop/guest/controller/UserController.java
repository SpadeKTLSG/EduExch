package com.shop.guest.controller;

import com.shop.common.utils.UserHolder;
import com.shop.pojo.Result;
import com.shop.pojo.dto.UserLocalDTO;
import com.shop.pojo.dto.UserLoginDTO;
import com.shop.serve.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 用户
 *
 * @author SK
 * @date 2024/05/31
 */
@Slf4j
@Tag(name = "User", description = "用户")
@RequestMapping("/guest/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;


    //! Func


    /**
     * 登录功能
     *
     * @return Token
     */
    @PostMapping("/login")
    @Operation(summary = "登录")
    @Parameters(@Parameter(name = "userLoginDTO", description = "用户登录DTO", required = true))
    public Result login(@RequestBody UserLoginDTO userLoginDTO, HttpSession session) {
        return userService.login(userLoginDTO, session);
    }
    //http://localhost:8086/guest/user/login


    /**
     * 发送手机验证码 并保存到redis
     */
    @PostMapping("code")
    @Operation(summary = "发送手机验证码")
    @Parameters(@Parameter(name = "phone", description = "手机号", required = true))
    public Result sendCode(@RequestParam("phone") String phone, HttpSession session) {
        return userService.sendCode(phone, session);
    }
    //http://localhost:8086/guest/user/code?phone=15985785169


    /**
     * 登出功能
     *
     * @return 无
     */
    @PostMapping("/logout")
    @Operation(summary = "退出")
    @Parameters(@Parameter(name = "无", description = "无", required = true))
    public Result logout() {
        return Result.success();
    }
    //http://localhost:8086/guest/user/logout


    //! ADD


    /**
     * 注册功能 需要注册三张表
     */
    @PostMapping("/register")
    @Operation(summary = "注册")
    @Parameters(@Parameter(name = "userLoginDTO", description = "用户登录DTO", required = true))
    public Result register(@RequestBody UserLoginDTO userLoginDTO, HttpSession session) {
        return userService.register(userLoginDTO, session);
    }
    //http://localhost:8086/guest/user/register


    /**
     * 获取当前用户LocalDTO
     */
    @GetMapping("/me")
    @Operation(summary = "获取当前用户")
    public Result me() {
        UserLocalDTO userLocalDTO = UserHolder.getUser();
        return Result.success(userLocalDTO);
    }
    //http://localhost:8086/guest/user/me


    //! DELETE

    /**
     * 注销自己
     */
    @DeleteMapping("/delete")
    @Operation(summary = "销号")
    public Result delete() {
        UserLocalDTO userLocalDTO = UserHolder.getUser();
        userService.removeById(userLocalDTO.getId());
        return Result.success();
    }


    //! UPDATE

    //改自己

    //! QUERY
}
