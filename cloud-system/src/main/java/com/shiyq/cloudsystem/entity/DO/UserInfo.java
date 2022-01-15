package com.shiyq.cloudsystem.entity.DO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author shiyq
 * @since 2022-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id（主键）
     */
    @TableId(value = "user_id", type = IdType.INPUT)
    private Integer userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像地址
     */
    private String avatarPath;

    /**
     * 已存储图像总数
     */
    private Long storedNum;

    /**
     * 已存储图像总大小（字节）
     */
    private Long storedSize;

    /**
     * 用户创建的所有次级路径，json数组
     */
    private String secondaryPath;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public UserInfo() {
    }

    public UserInfo(Integer userId) {
        this.userId = userId;
    }

    public UserInfo(Integer userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public UserInfo(Integer userId, String nickname, String secondaryPath) {
        this.userId = userId;
        this.nickname = nickname;
        this.secondaryPath = secondaryPath;
    }
}
