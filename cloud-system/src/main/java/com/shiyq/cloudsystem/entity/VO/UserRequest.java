package com.shiyq.cloudsystem.entity.VO;

import lombok.Data;

/**
 * 用户的登录、注册请求，携带的信息
 */
@Data
public class UserRequest {
    private String username;
    private String password;
    private String code;
}
