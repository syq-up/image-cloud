package com.shiyq.imagecloud.mapper;

import com.shiyq.imagecloud.entity.DO.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shiyq
 * @since 2022-01-05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 检查用户名（邮箱地址）是否已存在
     */
    boolean checkSameUsername(String username);

}
