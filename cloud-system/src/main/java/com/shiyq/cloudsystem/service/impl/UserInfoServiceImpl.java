package com.shiyq.cloudsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyq.cloudsystem.convert.UserInfoConvert;
import com.shiyq.cloudsystem.entity.DO.User;
import com.shiyq.cloudsystem.entity.DO.UserInfo;
import com.shiyq.cloudsystem.entity.DTO.UserContext;
import com.shiyq.cloudsystem.entity.VO.UserInfoVO;
import com.shiyq.cloudsystem.mapper.UserInfoMapper;
import com.shiyq.cloudsystem.mapper.UserMapper;
import com.shiyq.cloudsystem.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shiyq
 * @since 2022-01-05
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Value("${file.staticAccessUrlPrefix}")
    private String imageUrlPrefix;

    private UserMapper userMapper;
    private UserInfoMapper userInfoMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setUserInfoMapper(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    /**
     * 获取用户信息
     */
    public UserInfoVO getUserInfo() {
        User user = userMapper.selectById(UserContext.getCurrentUserId());
        UserInfo userInfo = userInfoMapper.selectById(UserContext.getCurrentUserId());
        // 默认头像or用户自定义头像
        if ("".equals(userInfo.getAvatarPath()))
            userInfo.setAvatarPath(imageUrlPrefix + "avatar_default.jpg");
        else
            userInfo.setAvatarPath(imageUrlPrefix + UserContext.getCurrentUserId() + "/" + userInfo.getAvatarPath());
        return UserInfoConvert.INSTANCE.userAndUserInfo2UserInfoVO(user, userInfo);
    }

}
