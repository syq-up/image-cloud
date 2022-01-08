package com.shiyq.imagecloud.service.impl;

import com.shiyq.imagecloud.convert.UserInfoConvert;
import com.shiyq.imagecloud.entity.DO.UserInfo;
import com.shiyq.imagecloud.entity.DTO.UserInfoDTO;
import com.shiyq.imagecloud.entity.DTO.XhrResult;
import com.shiyq.imagecloud.entity.VO.SecondaryPathVO;
import com.shiyq.imagecloud.mapper.UserInfoMapper;
import com.shiyq.imagecloud.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
     * @param id 用户id
     * @return 返回结果
     */
    public UserInfoDTO getUserInfo(long id) {
        return UserInfoConvert.INSTANCE.userInfoDOToDTO(userInfoMapper.selectById(id));
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
