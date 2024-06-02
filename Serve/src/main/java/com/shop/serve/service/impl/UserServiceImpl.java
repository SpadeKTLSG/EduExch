package com.shop.serve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.constant.PasswordConstant;
import com.shop.common.utils.RegexUtils;
import com.shop.pojo.Result;
import com.shop.pojo.dto.*;
import com.shop.pojo.entity.User;
import com.shop.pojo.entity.UserDetail;
import com.shop.pojo.entity.UserFunc;
import com.shop.serve.mapper.UserMapper;
import com.shop.serve.service.UserDetailService;
import com.shop.serve.service.UserFuncService;
import com.shop.serve.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.shop.common.utils.NewBeanUtils.getNullPropertyNames;
import static com.shop.common.utils.RedisConstants.*;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserFuncService userFuncService;
    @Autowired
    private UserDetailService userDetailService;


    @Transactional
    @Override
    public Result register(UserLoginDTO userLoginDTO, HttpSession session) {
        String phone = userLoginDTO.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.error("手机号格式错误！");
        }

        //从redis获取验证码并校验
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        String code = userLoginDTO.getCode();
        if (cacheCode == null || !cacheCode.equals(code)) {
            return Result.error("验证码错误!");
        }

        //校验账户是否已存在
        User userExist = query().eq("account", userLoginDTO.getAccount()).one();
        if (userExist != null) {
            return Result.error("账户已存在！");
        }

        //创建账户 + 账户具体信息 + 账户功能信息, 填充必须字段
        User user = User.builder()
                .account(userLoginDTO.getAccount())
                .password(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes())) //默认密码
                .phone(phone)
                .build();
        save(user);

        UserDetail userDetail = UserDetail.builder()
                .school("维也纳艺术学院")
                .createTime(LocalDateTime.now())
                .introduce("这个人很懒，什么都没留下")
                .build();
        userDetailService.save(userDetail);

        UserFunc userFunc = UserFunc.builder()
                .credit(114L)
                .build();
        userFuncService.save(userFunc);


        return Result.success();
    }

    @Override
    @Transactional
    public void updateUserGreatDTO(UserGreatDTO userGreatDTO) throws InstantiationException, IllegalAccessException {
        Optional<User> optionalUser = Optional.ofNullable(this.getOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, userGreatDTO.getAccount())));
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }


        // Split UserGreatDTO into three DTOs using the helper method
        UserDTO userDTO = createDTOFromUserGreatDTO(userGreatDTO, UserDTO.class);
        UserFuncDTO userFuncDTO = createDTOFromUserGreatDTO(userGreatDTO, UserFuncDTO.class);
        UserDetailDTO userDetailDTO = createDTOFromUserGreatDTO(userGreatDTO, UserDetailDTO.class);

        // Get null property names for each DTO
        String[] nullPN4User = getNullPropertyNames(userDTO);
        String[] nullPN4UserFunc = getNullPropertyNames(userFuncDTO);
        String[] nullPN4UserDetail = getNullPropertyNames(userDetailDTO);

        // Get target entities
        User u_target = optionalUser.get();
        UserFunc uf_target = userFuncService.getOne(Wrappers.<UserFunc>lambdaQuery().eq(UserFunc::getId, u_target.getId()));
        UserDetail ud_target = userDetailService.getOne(Wrappers.<UserDetail>lambdaQuery().eq(UserDetail::getId, u_target.getId()));

        // Update target entities
        BeanUtils.copyProperties(userDTO, u_target, nullPN4User);
        BeanUtils.copyProperties(userFuncDTO, uf_target, nullPN4UserFunc);
        BeanUtils.copyProperties(userDetailDTO, ud_target, nullPN4UserDetail);

        // Save updated entities
        this.updateById(u_target);
        userFuncService.updateById(uf_target);
        userDetailService.updateById(ud_target);
    }

    private <T> T createDTOFromUserGreatDTO(UserGreatDTO userGreatDTO, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        T dto = clazz.newInstance();
        BeanUtils.copyProperties(userGreatDTO, dto);
        return dto;
    }

    @Override
    public Result login(UserLoginDTO userLoginDTO, HttpSession session) {

        String phone = userLoginDTO.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.error("手机号格式错误！");
        }

        //从redis获取验证码并校验
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        String code = userLoginDTO.getCode();
        if (cacheCode == null || !cacheCode.equals(code)) {
            return Result.error("验证码错误!");
        }


        User user = query().eq("account", userLoginDTO.getAccount()).one();      //根据用户名查询用户
        if (user == null) {
            return Result.error("用户不存在！");
        }


        // 随机生成token，作为登录令牌
        String token = UUID.randomUUID().toString(true);
        UserLocalDTO userDTO = BeanUtil.copyProperties(user, UserLocalDTO.class); //将User对象转为HashMap存储
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));

        // 存储
        String tokenKey = LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);

        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);      // 设置token有效期

        return Result.success(token);
    }


    @Override
    public Result sendCode(String phone, HttpSession session) {

        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.error("手机号格式错误！");
        }

        String code = RandomUtil.randomNumbers(6); //生成验证码

        //保存验证码到 session
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);

        log.info("模拟发送短信验证码成功，验证码：{}", code);

        return Result.success();
    }


}
