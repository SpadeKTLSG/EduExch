package com.shop.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.pojo.Result;
import com.shop.pojo.dto.UserDTO;
import com.shop.pojo.dto.UserDetailDTO;
import com.shop.pojo.dto.UserFuncDTO;
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

import java.util.Optional;

import static com.shop.common.utils.NewBeanUtils.getNullPropertyNames;
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

        //在这里将userGreatDTO切为三个对象来使用BeanUtils.copyProperties
        // 1找到目标对象
        User u_target = optionalUser.get();
        UserFunc uf_target = userFuncService.getOne(Wrappers.<UserFunc>lambdaQuery().eq(UserFunc::getId, u_target.getId()));
        UserDetail ud_target = userDetailService.getOne(Wrappers.<UserDetail>lambdaQuery().eq(UserDetail::getId, u_target.getId()));

        // 2修改传递DTO
        UserDTO userDTO = new UserDTO();
        UserFuncDTO userFuncDTO = new UserFuncDTO();
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        BeanUtils.copyProperties(userGreatDTO, userDTO);
        BeanUtils.copyProperties(userGreatDTO, userFuncDTO);
        BeanUtils.copyProperties(userGreatDTO, userDetailDTO);

        //3分别判断每个对象的nullPN
        String[] nullPN4User = getNullPropertyNames(userDTO);
        String[] nullPN4UserFunc = getNullPropertyNames(userFuncDTO);
        String[] nullPN4UserDetail = getNullPropertyNames(userDetailDTO);


        //4分别更新目标对象
        BeanUtils.copyProperties(userDTO, u_target, nullPN4User);
        BeanUtils.copyProperties(userFuncDTO, uf_target, nullPN4UserFunc);
        BeanUtils.copyProperties(userDetailDTO, ud_target, nullPN4UserDetail);

        //5update
        userService.updateById(u_target);
        userFuncService.updateById(uf_target);
        userDetailService.updateById(ud_target);

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
