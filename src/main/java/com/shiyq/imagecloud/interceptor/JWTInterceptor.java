package com.shiyq.imagecloud.interceptor;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiyq.imagecloud.constant.HttpStatus;
import com.shiyq.imagecloud.entity.DTO.XhrResult;
import com.shiyq.imagecloud.util.JWTUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器，验证请求携带的token
 */
public class JWTInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String msg; // 错误信息
        String token = request.getHeader("Authorization");
        // token，移除前7位”Bearer “
        if (token == null || token.length() <= 7)
            msg = "Missing token!";
        else {
            try {
                JWTUtil.verify(token.substring(7));
                return true;
            } catch (TokenExpiredException e) {
                msg = "Token expired!";
            } catch (Exception e) {
                msg = "Bad token!";
            }
        }
        // 返回失败信息
        String json = new ObjectMapper().writeValueAsString(XhrResult.error(HttpStatus.UNAUTHORIZED, msg));
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }

}
