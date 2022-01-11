package com.shiyq.cloudsystem.entity.DTO;

import lombok.Data;

@Data
public class SettingDTO {
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
