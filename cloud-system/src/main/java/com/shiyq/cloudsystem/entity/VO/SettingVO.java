package com.shiyq.cloudsystem.entity.VO;

import lombok.Data;

/**
 * 1.设置获取、更新请求，携带的信息
 * 2.返回前端的设置信息
 */
@Data
public class SettingVO {
    // 布局设置
    private String theme;
    private String themeColor;
    private Boolean showDateInList;
    private Boolean showDateInRecycle;
    private Boolean fixedHeader;
    private Boolean dynamicTitle;
    // 操作设置
    private Boolean uploadDirectly;
}
