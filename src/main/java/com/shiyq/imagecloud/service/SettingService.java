package com.shiyq.imagecloud.service;

import com.shiyq.imagecloud.entity.DO.Setting;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyq.imagecloud.entity.DTO.SettingDTO;
import com.shiyq.imagecloud.entity.VO.SettingVO;

/**
 * <p>
 * 用户的设置表，包含一些布局设置、上传设置等。 服务类
 * </p>
 *
 * @author shiyq
 * @since 2022-01-04
 */
public interface SettingService extends IService<Setting> {

    /**
     * 获取用户设置
     * @param userId 用户id
     * @return 用户设置
     */
    SettingDTO getSettingByUserId(long userId);

    /**
     * 更新设置
     * @param settingVO 前端传来的更新设置
     * @return 是否更新成功
     */
    boolean updateSetting(SettingVO settingVO);

}
