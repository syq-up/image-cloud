package com.shiyq.cloudsystem.convert;

import com.alibaba.fastjson.JSON;
import com.shiyq.cloudsystem.entity.DO.UserInfo;
import com.shiyq.cloudsystem.entity.VO.UserInfoVO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class UserInfoConvert {
    public static UserInfoConvert INSTANCE = Mappers.getMapper(UserInfoConvert.class);

    // UserInfo --> UserInfoDTO
    @Mapping(source = "updateTime", target = "updateTime", dateFormat = "yyyy-MM-dd HH:mm")
    public abstract UserInfoVO userInfoDO2VO(UserInfo userInfo);
    // 解析json字符串为list
    @AfterMapping
    public void userInfoDOToDTOAfter(UserInfo userInfo, @MappingTarget UserInfoVO userInfoVO) {
        userInfoVO.setSecondaryPathList(JSON.parseArray(userInfo.getSecondaryPath(), String.class));
    }
}
