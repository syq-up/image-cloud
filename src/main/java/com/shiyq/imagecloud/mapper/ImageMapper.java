package com.shiyq.imagecloud.mapper;

import com.shiyq.imagecloud.entity.DO.Image;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shiyq
 * @since 2022-01-02
 */
@Mapper
public interface ImageMapper extends BaseMapper<Image> {

    /**
     * 通过【自定义SQL】查询【回收站列表】（【逻辑删除】的记录）
     * @param sql 自定义sql
     * @return 回收站列表
     */
    @Select({"${sql}"})
    @ResultType(ArrayList.class)
    List<Image> getRecycleListBySql(@Param("sql") String sql);

    /**
     * 查询回收站记录总数（逻辑删除的记录）
     * @return 记录总数
     */
    @Select({"SELECT COUNT(*) FROM `image` WHERE `deleted` = 1;"})
    long getTotalRecycle();

    /**
     * 恢复逻辑删除的记录
     * @return 行数
     */
    @Update({"${sql}"})
    int restoreBySql(@Param("sql") String sql);

}
