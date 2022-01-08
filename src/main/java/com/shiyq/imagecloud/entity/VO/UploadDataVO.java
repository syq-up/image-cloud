package com.shiyq.imagecloud.entity.VO;

import lombok.Data;

@Data
public class UploadDataVO {
    // 用户id
    private Integer userId;
    // 存储图像的次级路径
    private String secondaryPath;
    // 网络图像链接
    private String webImageUrl;
}
