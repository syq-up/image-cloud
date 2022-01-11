package com.shiyq.cloudsystem.interceptor;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiyq.cloudsystem.constant.HttpStatus;
import com.shiyq.cloudsystem.entity.DTO.UserContext;
import com.shiyq.cloudsystem.entity.DTO.XhrResult;
import com.shiyq.cloudsystem.util.JWTUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器，验证请求携带的token
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String msg; // 错误信息
        String token = request.getHeader("Authorization");
        // token，前7位”Bearer “
        if (token == null || token.length() <= 7)
            msg = "Missing token! Please sign in.";
        else {
            try {
                // 解析出userId，放入登录用户的上下文
                UserContext.add(JWTUtil.verify(token.substring(7)).getClaim("userId").asString());
                return true;
            } catch (TokenExpiredException e) {
                msg = "Token expired! Please sign in again.";
            } catch (Exception e) {
                msg = "Bad token! Please sign in again.";
            }
        }
        // 返回失败信息
        String json = new ObjectMapper().writeValueAsString(XhrResult.error(HttpStatus.UNAUTHORIZED, msg));
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求结束，移除当前用户上下文
        UserContext.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
