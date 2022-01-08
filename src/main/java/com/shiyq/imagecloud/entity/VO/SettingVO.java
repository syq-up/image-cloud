package com.shiyq.imagecloud.entity.VO;

import lombok.Data;

@Data
public class SettingVO {
    private String userId;
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
