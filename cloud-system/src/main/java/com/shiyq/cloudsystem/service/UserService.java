package com.shiyq.cloudsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyq.cloudsystem.entity.DO.User;
import com.shiyq.cloudsystem.entity.VO.UserVO;
import com.shiyq.cloudsystem.entity.VO.UserRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shiyq
 * @since 2022-01-05
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     * @param userRequest 用户登录验证信息
     * @return 成功返回用户信息，失败返回null
     */
    UserVO signIn(UserRequest userRequest);

    /**
     * 用户注册
     * @param userRequest 用户注册信息
     * @return 成功返回用户信息，失败返回null
     */
    UserVO signup(UserRequest userRequest);

    /**
     * 检查用户名（邮箱地址）是否已存在
     */
    boolean checkSameUsername(String username);

    /**
     * 发送邮箱验证码
     * @param emailTo 接收者邮箱
     * @return 是否成功
     */
    boolean sendEmailVerificationCode(String emailTo);

    /**
     * 更新用户密码
     * @return 数据库受影响的行数 0：与原密码相同，1：已修改，-1：原密码错误
     */
    int updatePassword(String oldPassword, String newPassword);
}
