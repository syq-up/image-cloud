package com.shiyq.cloudsystem.entity.DO;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 图像信息表
 * </p>
 *
 * @author shiyq
 * @since 2022-01-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 图像存储路径
     */
    private String path;

    /**
     * 图像大小
     */
    private Integer size;

    /**
     * 逻辑删除（0否1是）
     */
    @TableLogic
    private String deleted;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public Image() {
    }

    public Image(Long id, String path, Integer size, Integer userId) {
        this.id = id;
        this.path = path;
        this.size = size;
        this.userId = userId;
    }
}
