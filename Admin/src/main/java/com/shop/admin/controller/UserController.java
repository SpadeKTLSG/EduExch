package com.shop.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.pojo.Result;
import com.shop.pojo.dto.UserGreatDTO;
import com.shop.pojo.entity.User;
import com.shop.pojo.entity.UserDetail;
import com.shop.pojo.entity.UserFunc;
import com.shop.pojo.vo.UserVO;
import com.shop.serve.service.UserDetailService;
import com.shop.serve.service.UserFuncService;
import com.shop.serve.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

import static com.shop.common.utils.NewBeanUtils.getNullPropertyNamesPlus;
import static com.shop.common.utils.SystemConstants.MAX_PAGE_SIZE;

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
    @Autowired
    private UserFuncService userFuncService;
    @Autowired
    private UserDetailService userDetailService;


    //! Func


    //! ADD
    //管理员手动添加用户: 未实现

    //! DELETE
    //管理员手动删除用户: 未实现

    //! UPDATE

    /**
     * 选择性更新用户信息
     */
    @Transactional
    @PutMapping("/update")
    @Operation(summary = "选择性更新用户信息")
    @Parameters(@Parameter(name = "userGreatDTO", description = "用户更新DTO", required = true))
    public Result update(@RequestBody UserGreatDTO userGreatDTO) {

        Optional<User> optionalUser = Optional.ofNullable(userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, userGreatDTO.getAccount())));
        if (optionalUser.isEmpty()) {
            return Result.error("用户不存在");
        }

        //选择性更新三张表 userService, userFuncService, userDetailService
        User u2 = optionalUser.get();
        String[] nullPropertyNames = getNullPropertyNamesPlus(userGreatDTO, User.class, UserFunc.class, UserDetail.class);

        BeanUtils.copyProperties(userGreatDTO, u2, nullPropertyNames);
        userService.updateById(u2);

        UserFunc uf2 = userFuncService.getOne(Wrappers.<UserFunc>lambdaQuery().eq(UserFunc::getId, u2.getId()));
        BeanUtils.copyProperties(userGreatDTO, uf2, nullPropertyNames);
        uf2.setId(u2.getId());
        if (!Arrays.equals(nullPropertyNames, getNullPropertyNamesPlus(uf2, UserFunc.class))) {
            userFuncService.updateById(uf2);
        }
        UserDetail ud2 = userDetailService.getOne(Wrappers.<UserDetail>lambdaQuery().eq(UserDetail::getId, u2.getId()));
        BeanUtils.copyProperties(userGreatDTO, ud2, nullPropertyNames);
        ud2.setId(u2.getId());
        if (!Arrays.equals(nullPropertyNames, getNullPropertyNamesPlus(ud2, UserDetail.class))) {
            userDetailService.updateById(ud2); //bug here : if nothing changed, it will still update then fail
        }

        return Result.success();
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
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, account));
        if (user == null) {
            return Result.error("用户不存在");
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return Result.success(userVO);
    }
    //http://localhost:8085/admin/user/cwxtlsg


    /**
     * ID查用户
     */
    @GetMapping("/specify/{id}")
    @Operation(summary = "ID查用户")
    @Parameters(@Parameter(name = "id", description = "用户ID", required = true))
    public Result getById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return Result.success(userVO);
    }
    //http://localhost:8085/admin/user/specify/1

    /**
     * 分页查全部用户
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    @Parameters(@Parameter(name = "current", description = "当前页", required = true))
    public Result pageQuery(@RequestParam(value = "current", defaultValue = "1") Integer current) {

        Page<UserVO> userVOPage = (Page<UserVO>) userService.page(new Page<>(current, MAX_PAGE_SIZE)).convert(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        });

        return Result.success(userVOPage);
    }
    //http://localhost:8085/admin/user/page

}
