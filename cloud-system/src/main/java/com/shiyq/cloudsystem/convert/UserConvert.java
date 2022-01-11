package com.shiyq.cloudsystem.convert;

import com.shiyq.cloudsystem.entity.DO.User;
import com.shiyq.cloudsystem.entity.DTO.UserTokenDTO;
import com.shiyq.cloudsystem.entity.VO.UserVO;
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

    // User --> UserTokenDTO
    public abstract UserTokenDTO userDOToDTO(User user);
    @AfterMapping
    public void userDOToDTOAfter(User user, @MappingTarget UserTokenDTO userTokenDTO) {
        // 类型转换后，添加token属性信息
        Map<String, String> payload = new HashMap<>();
        payload.put("userId", String.valueOf(user.getId()));
        userTokenDTO.setAccessToken(JWTUtil.getToken(payload));
    }

    // UserVO --> User
    public abstract User userVOtoDO(UserVO userVO);
}
