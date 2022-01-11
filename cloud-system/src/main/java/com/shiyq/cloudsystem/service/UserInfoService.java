package com.shiyq.cloudsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyq.cloudsystem.entity.DO.UserInfo;
import com.shiyq.cloudsystem.entity.DTO.UserInfoDTO;
import com.shiyq.cloudsystem.entity.VO.SecondaryPathVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shiyq
 * @since 2022-01-05
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 获取用户信息
     */
    UserInfoDTO getUserInfo();

    /**
     * 更新用户信息：次级路径信息
     * @param secondaryPathVO 新的次级路径信息
     * @return 是否更新成功
     */
    boolean updateUserInfo(SecondaryPathVO secondaryPathVO);

}
