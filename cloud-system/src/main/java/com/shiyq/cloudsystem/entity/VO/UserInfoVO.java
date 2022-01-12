package com.shiyq.cloudsystem.entity.VO;

import lombok.Data;
import java.util.List;

/**
 * 返回前端，用户信息
 */
@Data
public class UserInfoVO {
    private Long storedSize;
    private List<String> secondaryPathList;
    private String updateTime;
}
