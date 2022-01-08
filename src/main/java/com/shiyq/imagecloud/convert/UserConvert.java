package com.shiyq.imagecloud.convert;

import com.shiyq.imagecloud.entity.DO.User;
import com.shiyq.imagecloud.entity.DTO.UserDTO;
import com.shiyq.imagecloud.entity.VO.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class UserConvert {
    public static UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    // User --> UserDTO
    public abstract UserDTO userDOToDTO(User user);

    // UserVO --> User
    public abstract User userVOtoDO(UserVO userVO);
}
