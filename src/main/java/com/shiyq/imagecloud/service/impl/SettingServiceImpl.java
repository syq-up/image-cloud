package com.shiyq.imagecloud.service.impl;

import com.shiyq.imagecloud.convert.SettingConvert;
import com.shiyq.imagecloud.entity.DO.Setting;
import com.shiyq.imagecloud.entity.DTO.SettingDTO;
import com.shiyq.imagecloud.entity.DTO.UserContext;
import com.shiyq.imagecloud.entity.VO.SettingVO;
import com.shiyq.imagecloud.mapper.SettingMapper;
import com.shiyq.imagecloud.service.SettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户的设置表，包含一些布局设置、上传设置等。 服务实现类
 * </p>
 *
 * @author shiyq
 * @since 2022-01-04
 */
@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, Setting> implements SettingService {

    private SettingMapper settingMapper;

    @Autowired
    public void setSettingMapper(SettingMapper settingMapper) {
        this.settingMapper = settingMapper;
    }

    /**
     * 获取用户设置
     */
    @Override
    public SettingDTO getSettingByUserId() {
        return SettingConvert.INSTANCE.settingToSettingDTO(settingMapper.selectById(UserContext.getCurrentUserId()));
    }

    /**
     * 更新设置
     * @param settingVO 前端传来的更新设置
     * @return 是否更新成功
     */
    @Override
    public boolean updateSetting(SettingVO settingVO) {
        return settingMapper.updateById(SettingConvert.INSTANCE.settingVOToSettingDO(settingVO)) > 0;
    }
}
