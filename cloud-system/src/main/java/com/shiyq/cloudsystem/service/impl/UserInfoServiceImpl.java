package com.shiyq.cloudsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyq.cloudsystem.convert.UserInfoConvert;
import com.shiyq.cloudsystem.entity.DO.UserInfo;
import com.shiyq.cloudsystem.entity.DTO.UserContext;
import com.shiyq.cloudsystem.entity.VO.UserInfoVO;
import com.shiyq.cloudsystem.mapper.UserInfoMapper;
import com.shiyq.cloudsystem.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private UserInfoMapper userInfoMapper;

    @Autowired
    public void setUserInfoMapper(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    /**
     * 获取用户信息
     */
    public UserInfoVO getUserInfo() {
        return UserInfoConvert.INSTANCE.userInfoDO2VO(userInfoMapper.selectById(UserContext.getCurrentUserId()));
    }

}
