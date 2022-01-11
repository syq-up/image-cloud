package com.shiyq.cloudsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyq.cloudsystem.convert.UserInfoConvert;
import com.shiyq.cloudsystem.entity.DO.UserInfo;
import com.shiyq.cloudsystem.entity.DTO.UserContext;
import com.shiyq.cloudsystem.entity.DTO.UserInfoDTO;
import com.shiyq.cloudsystem.entity.VO.SecondaryPathVO;
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
    public UserInfoDTO getUserInfo() {
        return UserInfoConvert.INSTANCE.userInfoDOToDTO(userInfoMapper.selectById(UserContext.getCurrentUserId()));
    }

    /**
     * 更新用户信息：次级路径信息
     * @param secondaryPathVO 新的次级路径信息
     * @return 是否更新成功
     */
    public boolean updateUserInfo(SecondaryPathVO secondaryPathVO) {
        return userInfoMapper.updateById(UserInfoConvert.INSTANCE.secondaryPathVOToUserInfoDO(secondaryPathVO)) > 0;
    }

}
