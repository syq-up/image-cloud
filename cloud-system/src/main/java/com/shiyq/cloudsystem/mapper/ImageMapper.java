package com.shiyq.cloudsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiyq.cloudsystem.entity.DO.Image;
import org.apache.ibatis.annotations.Mapper;

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
     * 查询回收站所有记录
     */
    List<Image> getAllRecycleList();

    /**
     * 查询回收站记录总数（逻辑删除的记录）
     * @return 记录总数
     */
    long getTotalRecycle(int userId);

    /**
     * 恢复逻辑删除的记录
     */
    boolean restoreById(long id);

    /**
     * 物理删除图像
     */
    boolean realDeleteById(long id);

}
