package com.shiyq.cloudsystem.entity.VO;

import lombok.Data;

/**
 * 图像上传请求，携带的信息
 */
@Data
public class UploadRequest {
    // 存储图像的次级路径
    private String secondaryPath;
    // 网络图像链接
    private String webImageUrl;
}
