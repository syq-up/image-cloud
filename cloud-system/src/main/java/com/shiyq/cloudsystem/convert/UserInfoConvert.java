package com.shiyq.cloudsystem.convert;

import com.alibaba.fastjson.JSON;
import com.shiyq.cloudsystem.entity.DO.UserInfo;
import com.shiyq.cloudsystem.entity.DTO.UserContext;
import com.shiyq.cloudsystem.entity.DTO.UserInfoDTO;
import com.shiyq.cloudsystem.entity.VO.SecondaryPathVO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class UserInfoConvert {
    public static UserInfoConvert INSTANCE = Mappers.getMapper(UserInfoConvert.class);

    // SecondaryPathVO --> UserInfo
    public abstract UserInfo secondaryPathVOToUserInfoDO(SecondaryPathVO secondaryPathVO);
    @AfterMapping
    public void secondaryPathVOToUserInfoDOAfter(SecondaryPathVO secondaryPathVO, @MappingTarget UserInfo userInfo) {
        userInfo.setUserId(UserContext.getCurrentUserId());
    }

    // UserInfo --> UserInfoDTO
    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(source = "updateTime", target = "updateTime", dateFormat = "yyyy-MM-dd HH:mm")
    public abstract UserInfoDTO userInfoDOToDTO(UserInfo userInfo);
    // 解析json字符串为list
    @AfterMapping
    public void userInfoDOToDTOAfter(UserInfo userInfo, @MappingTarget UserInfoDTO userInfoDTO) {
        userInfoDTO.setSecondaryPathList(JSON.parseArray(userInfo.getSecondaryPath(), String.class));
    }
}
