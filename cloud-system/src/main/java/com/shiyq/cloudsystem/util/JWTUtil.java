package com.shiyq.cloudsystem.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JWTUtil {

    // jwt签名私匙
    private static final String SIGNATURE = "a#*&BK&2356QKV**(!~kb124e^B";

    /**
     * 生成token
     * @param payload 存入token的值
     * @return token
     */
    public static String getToken(Map<String, String> payload) {
        // token 14天过期
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 14);
        // 创建jwt builder
        JWTCreator.Builder builder = JWT.create();
        // payload
        payload.forEach(builder::withClaim);
        // 生成并返回token
        return builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(SIGNATURE));
    }

    /**
     * 验证token
     * @param token 待验证token
     * @return payload存储的值
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
    }

}
