package com.shiyq.cloudsystem.entity.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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

}
