package com.shiyq.imagecloud.mapper;

import com.shiyq.imagecloud.entity.DO.Setting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户的设置表，包含一些布局设置、上传设置等。 Mapper 接口
 * </p>
 *
 * @author shiyq
 * @since 2022-01-04
 */
@Mapper
public interface SettingMapper extends BaseMapper<Setting> {

}
