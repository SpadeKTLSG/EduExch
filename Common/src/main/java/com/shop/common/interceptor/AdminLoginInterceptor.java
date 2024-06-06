package com.shop.common.interceptor;


import com.shop.common.context.EmployeeHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用户端登录拦截器
 *
 * @author SK
 * @date 2024/06/06
 */
public class AdminLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (EmployeeHolder.getEmployee() == null) {// 判断是否需要拦截
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
