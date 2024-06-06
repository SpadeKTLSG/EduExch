package com.shop.common.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.shop.common.context.UserHolder;
import com.shop.common.exception.AccountNotFoundException;
import com.shop.common.exception.NotLoginException;
import com.shop.pojo.dto.UserLocalDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.shop.common.constant.MessageConstants.ACCOUNT_NOT_FOUND;
import static com.shop.common.constant.MessageConstants.USER_NOT_LOGIN;
import static com.shop.common.constant.RedisConstants.LOGIN_USER_KEY;
import static com.shop.common.constant.RedisConstants.LOGIN_USER_TTL;


/**
 * 刷新token拦截器
 *
 * @author SK
 * @date 2024/06/06
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取请求头中的token
        String token = request.getHeader("authorization");
        if (StrUtil.isBlank(token)) throw new NotLoginException(USER_NOT_LOGIN);


        // 2. Remove the "Bearer " prefix from the token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 2.基于TOKEN获取redis中的用户
        String key = LOGIN_USER_KEY + token;
        System.out.println("Key: " + key);


        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        System.out.println("User data: " + userMap);

        // 4. If the user data is empty, check if the key exists in Redis
        if (userMap.isEmpty()) {
            throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);
        }
        // 5.将查询到的hash数据转为UserDTO
        UserLocalDTO userLocalDTO = BeanUtil.fillBeanWithMap(userMap, new UserLocalDTO(), false);
        // 6.存在，保存用户信息到 ThreadLocal
        UserHolder.saveUser(userLocalDTO);
        // 7.刷新token有效期
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
        // 8.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        UserHolder.removeUser();
    }
}
