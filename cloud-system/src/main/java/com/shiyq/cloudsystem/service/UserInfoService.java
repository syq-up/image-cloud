package com.shiyq.cloudsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyq.cloudsystem.entity.DO.UserInfo;
import com.shiyq.cloudsystem.entity.VO.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    UserInfoVO getUserInfo();

    /**
     * 更新用户昵称
     */
    int updateUserInfo(String nickname);

    /**
     * 上传用户头像
     */
    String uploadUserAvatar(MultipartFile avatarFile) throws IOException;

}
