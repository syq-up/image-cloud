package com.shiyq.cloudsystem.convert;

import com.alibaba.fastjson.JSON;
import com.shiyq.cloudsystem.entity.DO.User;
import com.shiyq.cloudsystem.entity.DO.UserInfo;
import com.shiyq.cloudsystem.entity.VO.UserInfoVO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class UserInfoConvert {
    public static UserInfoConvert INSTANCE = Mappers.getMapper(UserInfoConvert.class);

    // UserInfo --> UserInfoDTO
    @Mappings({
            @Mapping(source = "user.username", target = "username"),
            @Mapping(source = "userInfo.nickname", target = "nickname"),
            @Mapping(source = "userInfo.avatarPath", target = "avatarUrl"),
            @Mapping(source = "userInfo.storedSize", target = "storedSize"),
            @Mapping(source = "user.createTime", target = "createTime", dateFormat = "yyyy-MM-dd HH:mm")
    })
    public abstract UserInfoVO userAndUserInfo2UserInfoVO(User user, UserInfo userInfo);
    // 解析json字符串为list
    @AfterMapping
    public void userAndUserInfo2UserInfoVOAfter(User user, UserInfo userInfo, @MappingTarget UserInfoVO userInfoVO) {
        userInfoVO.setSecondaryPathList(JSON.parseArray(userInfo.getSecondaryPath(), String.class));
    }
}
