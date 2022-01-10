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
     * 查询【回收站列表】（【逻辑删除】的记录）
     */
    List<Image> getRecycleListByIdAndPageNum(int userId, int limit, long offset);

    /**
     * 查询回收站记录总数（逻辑删除的记录）
     * @return 记录总数
     */
    long getTotalRecycle(int userId);

    /**
     * 恢复逻辑删除的记录
     */
    boolean restoreById(long id);

}
