package com.shiyq.cloudsystem.entity.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 用户的设置表，包含一些布局设置、上传设置等。
 * </p>
 *
 * @author shiyq
 * @since 2022-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Setting implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id（主键）
     */
    @TableId(value = "user_id", type = IdType.INPUT)
    private Integer userId;

    /**
     * 主题（默认“dark”）
     */
    private String theme;

    /**
     * 主题颜色（十六进制）
     */
    private String themeColor;

    /**
     * 是否在图像列表显示时间标签（0否1是，默认1）
     */
    private String showDateInList;

    /**
     * 是否以文件夹样式显示图像列表（0否1是，默认0）
     */
    private String folderStyleInList;

    /**
     * 是否在图像列表的文件夹样式下分割文件夹和图像（0否1是，默认1）
     */
    private String showDividerInList;

    /**
     * 是否在回收站列表显示时间标签（0否1是，默认1）
     */
    private String showDateInRecycle;

    /**
     * 是否以文件夹样式显示回收站列表（0否1是，默认0）
     */
    private String folderStyleInRecycle;

    /**
     * 是否在回收站列表的文件夹样式下分割文件夹和图像（0否1是，默认1）
     */
    private String showDividerInRecycle;

    /**
     * 是否固定头部（0否1是，默认0）
     */
    private String fixedHeader;

    /**
     * 是否显示动态标题（0否1是，默认1）
     */
    private String dynamicTitle;

    /**
     * 是否立即上传图像（0否1是，默认1）
     */
    private String uploadDirectly;

    public Setting() {
    }

    public Setting(Integer userId) {
        this.userId = userId;
    }
}
