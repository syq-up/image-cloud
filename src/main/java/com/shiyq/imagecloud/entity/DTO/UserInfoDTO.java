package com.shiyq.imagecloud.entity.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoDTO {
    private Long storedSize;
    private List<String> secondaryPathList;
    private String createTime;
    private String updateTime;
}
