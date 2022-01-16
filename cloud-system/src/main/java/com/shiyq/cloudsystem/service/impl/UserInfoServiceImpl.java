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
import com.shiyq.cloudsystem.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @Value("${file.uploadFolder}")
    private String uploadFolder;
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
        if (!"".equals(userInfo.getAvatarPath()))
            userInfo.setAvatarPath(imageUrlPrefix + String.format("%06d", UserContext.getCurrentUserId()) + "/"
                    + userInfo.getAvatarPath());
        return UserInfoConvert.INSTANCE.userAndUserInfo2UserInfoVO(user, userInfo);
    }

    /**
     * 更新用户昵称
     */
    public int updateUserInfo(String nickname) {
        return userInfoMapper.updateById(new UserInfo(UserContext.getCurrentUserId(), nickname));
    }

    /**
     * 上传用户头像
     */
    @Override
    public String uploadUserAvatar(MultipartFile avatarFile) throws IOException {
        // 上传文件
        String uploadPath = uploadFolder + String.format("%06d", UserContext.getCurrentUserId());
        String filename = FileUploadUtil.INSTANCE.uploadFile("", avatarFile);
        // 更新数据库
        UserInfo userInfo = new UserInfo(UserContext.getCurrentUserId());
        userInfo.setAvatarPath(filename);
        userInfoMapper.updateById(userInfo);
        // 返回前端头像url
        return imageUrlPrefix + String.format("%06d", UserContext.getCurrentUserId()) + "/" + filename;
    }

}
