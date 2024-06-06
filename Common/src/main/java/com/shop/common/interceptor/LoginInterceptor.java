package com.shop.common.interceptor;


import com.shop.common.context.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器
 *
 * @author SK
 * @date 2024/06/06
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (UserHolder.getUser() == null) {// 判断是否需要拦截（ThreadLocal中是否有用户）
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
