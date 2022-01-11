package com.shiyq.cloudsystem.convert;

import com.shiyq.cloudsystem.entity.DO.User;
import com.shiyq.cloudsystem.entity.VO.UserVO;
import com.shiyq.cloudsystem.entity.VO.UserRequest;
import com.shiyq.cloudsystem.util.JWTUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.Map;

@Mapper
public abstract class UserConvert {
    public static UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    // UserDO --> UserVO
    public abstract UserVO userDO2VO(User user);
    @AfterMapping
    public void userDO2VOAfter(User user, @MappingTarget UserVO userVO) {
        // 类型转换后，添加token属性信息
        Map<String, String> payload = new HashMap<>();
        payload.put("userId", String.valueOf(user.getId()));
        userVO.setAccessToken(JWTUtil.getToken(payload));
    }

    // UserVO --> User
    public abstract User userRequest2UserDO(UserRequest userRequest);
}
