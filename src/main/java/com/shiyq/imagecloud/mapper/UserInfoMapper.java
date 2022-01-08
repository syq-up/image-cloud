package com.shiyq.imagecloud.mapper;

import com.shiyq.imagecloud.entity.DO.UserInfo;
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
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 更新已存储图像总大小（在原值上追加新文件大小）
     * @param increase 追加值
     * @return 是否成功
     */
    boolean updateStoredSizeByIncrease(long userId, long increase);

}
