package com.shop.common.interceptor;


import com.shop.common.constant.JwtClaimsConstant;
import com.shop.common.context.UserHolder;
import com.shop.common.properties.JwtProperties;
import com.shop.common.utils.JwtUtil;
import com.shop.pojo.dto.UserLocalDTO;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * jwt令牌校验的拦截器
 *
 * @author SK
 * @date 2024/06/05
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;


    /**
     * 校验jwt
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  处理器
     * @return 是否放行
     * @throws Exception 异常
     */
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {//当前拦截到的不是动态方法，直接放行
            return true;
        }

        String token = request.getHeader(jwtProperties.getAdminTokenName());//获取令牌

        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());

            UserHolder.saveUser(UserLocalDTO.builder().id(empId).build());
            log.info("当前员工id：{}", empId);
            return true;

        } catch (Exception ex) {
            response.setStatus(401);
            return true;
        }

    }
}
