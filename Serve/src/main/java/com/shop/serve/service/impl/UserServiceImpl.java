package com.shop.serve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.constant.PasswordConstant;
import com.shop.common.utils.RegexUtils;
import com.shop.pojo.Result;
import com.shop.pojo.dto.UserDTO;
import com.shop.pojo.dto.UserLoginDTO;
import com.shop.pojo.entity.User;
import com.shop.serve.mapper.UserMapper;
import com.shop.serve.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.shop.common.utils.RedisConstants.*;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


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

        User user = User.builder()
                .account(userLoginDTO.getAccount())
                .password(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes())) //默认密码
                .phone(phone)
                .build();
        save(user);
        return Result.success();
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
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class); //将User对象转为HashMap存储
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
