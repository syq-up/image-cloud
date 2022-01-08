package com.shiyq.imagecloud.service;

import com.shiyq.imagecloud.entity.DO.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyq.imagecloud.entity.DTO.UserDTO;
import com.shiyq.imagecloud.entity.VO.UserVO;

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
     * @param userVO 用户登录验证信息
     * @return 成功返回用户信息，失败返回null
     */
    UserDTO signIn(UserVO userVO);

    /**
     * 用户注册
     * @param userVO 用户注册信息
     * @return 成功返回用户信息，失败返回null
     */
    UserDTO signup(UserVO userVO);

    /**
     * 发送邮箱验证码
     * @param emailTo 接收者邮箱
     * @return 是否成功
     */
    boolean sendEmailVerificationCode(String emailTo);
}
