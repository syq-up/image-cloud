package com.shiyq.cloudsystem.entity.DO;

import com.baomidou.mybatisplus.annotation.*;
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
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    // TODO id生成策略不太合适，想找一种类似于雪花算法，但较短的数字id，
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名（邮箱地址）
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 逻辑删除（0否1是）
     */
    @TableLogic
    private String deleted;

    /**
     * 账号创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public User() {
    }

    public User(Integer id, String password) {
        this.id = id;
        this.password = password;
    }
}
