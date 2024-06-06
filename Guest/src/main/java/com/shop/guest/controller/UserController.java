package com.shop.guest.controller;

import com.shop.common.context.UserHolder;
import com.shop.pojo.Result;
import com.shop.pojo.dto.UserGreatDTO;
import com.shop.pojo.dto.UserLocalDTO;
import com.shop.pojo.dto.UserLoginDTO;
import com.shop.serve.service.UserFollowService;
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
    @Autowired
    private UserFollowService userFollowService;


    //! Func


    /**
     * 登录功能
     */
    @PostMapping("/login")
    @Operation(summary = "登录")
    @Parameters(@Parameter(name = "userLoginDTO", description = "用户登录DTO", required = true))
    public Result login(@RequestBody UserLoginDTO userLoginDTO, HttpSession session) {
        return Result.success(userService.login(userLoginDTO, session));
    }
    //http://localhost:8086/guest/user/login


    /**
     * 发送手机验证码 并保存到redis
     */
    @PostMapping("code")
    @Operation(summary = "发送手机验证码")
    @Parameters(@Parameter(name = "phone", description = "手机号", required = true))
    public Result sendCode(@RequestParam("phone") String phone, HttpSession session) {
        return Result.success(userService.sendCode(phone, session));
    }
    //http://localhost:8086/guest/user/code?phone=15985785169


    /**
     * 登出功能
     */
    @PostMapping("/logout")
    @Operation(summary = "退出")
    @Parameters(@Parameter(name = "无", description = "无", required = true))
    public Result logout() {
        userService.logout();
        return Result.success();
    }
    //http://localhost:8086/guest/user/logout


    /**
     * 关注功能
     */
    @PutMapping("follow/{id}/{isFollow}")
    @Operation(summary = " 用户关注")
    @Parameters({@Parameter(name = "id", description = "被关注用户id", required = true), @Parameter(name = "isFollow", description = "是否关注", required = true)})
    public Result follow(@PathVariable("id") Long followUserId, @PathVariable("isFollow") Boolean isFollow) {
        return userFollowService.follow(followUserId, isFollow);
    }
    //http://localhost:8086/guest/user/follow/2/true


    /**
     * 是否关注
     */
    @GetMapping("follow/ornot/{id}")
    @Operation(summary = "是否关注")
    @Parameters(@Parameter(name = "id", description = "被关注用户id", required = true))
    public Result isFollow(@PathVariable("id") Long followUserId) {
        return userFollowService.isFollow(followUserId);
    }
    //http://localhost:8086/guest/user/follow/ornot/2


    /**
     * 查询共同关注
     */
    @GetMapping("follow/share/{id}")
    @Operation(summary = "关注的人")
    @Parameters(@Parameter(name = "id", description = "用户id", required = true))
    public Result shareFollow(@PathVariable("id") Long id) {
        return userFollowService.shareFollow(id);
    }
    //http://localhost:8086/guest/user/follow/share/2


    /**
     * 签到
     */
    @PostMapping("/sign/add")
    @Operation(summary = "签到")
    public Result sign() {
        userService.sign();
        return Result.success();
    }
    //http://localhost:8086/guest/user/sign/add


    /**
     * 签到次数
     */
    @GetMapping("/sign/count")
    @Operation(summary = "签到次数")
    public Result signCount() {
        return Result.success(userService.signCount());
    }
    //http://localhost:8086/guest/user/sign/count


    //! ADD


    /**
     * 注册功能 需要注册三张表
     */
    @PostMapping("/register")
    @Operation(summary = "注册")
    @Parameters(@Parameter(name = "userLoginDTO", description = "用户登录DTO", required = true))
    public Result register(@RequestBody UserLoginDTO userLoginDTO, HttpSession session) {
        userService.register(userLoginDTO, session);
        return Result.success();
    }
    //http://localhost:8086/guest/user/register


    /**
     * 获取当前用户
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
    public Result killMyAccount() {
        userService.killMyAccount();
        return Result.success();
    }
    //http://localhost:8086/guest/user/delete


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
    //http://localhost:8086/guest/user/update


    //! QUERY

    /**
     * 查自己全部信息
     */
    @GetMapping("/info")
    @Operation(summary = "查用户自己全部信息")
    public Result info() {
        return Result.success(userService.info());
    }
    //http://localhost:8086/guest/user/info

}
