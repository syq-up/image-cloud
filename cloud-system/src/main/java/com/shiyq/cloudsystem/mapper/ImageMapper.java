package com.shiyq.cloudsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiyq.cloudsystem.entity.DO.Image;
import com.shiyq.cloudsystem.entity.VO.PageVO;
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
     * 根据条件，分页查询图像列表
     * @param path      条件之一，为null则忽略
     * @param pageNum   条件之一，页数
     * @param deleted   条件之一，查询（未）逻辑删除的记录（0否1是）
     */
    List<Image> getImageListByCondition(int userId, String path, int pageNum, int pageSize, String deleted);

    /**
     * 根据条件，查询图像记录总数
     * @param path 为null则忽略，不为null则做为条件
     * @param deleted 是否查询已逻辑删除的记录总数（0否1是）
     * @return 记录总数
     */
    long getTotalByCondition(int userId, String path, String deleted);

    /**
     * 查询回收站所有记录
     */
    List<Image> getAllRecycleList();

    /**
     * 恢复逻辑删除的记录
     */
    boolean restoreById(long id);

    /**
     * 物理删除图像
     */
    boolean realDeleteById(long id);

    /**
     * 逻辑删除时，自动填充其他字段
     */
    int deleteByIdWithFill(Image image);

}
