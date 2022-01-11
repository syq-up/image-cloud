package com.shiyq.cloudsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyq.cloudsystem.convert.SettingConvert;
import com.shiyq.cloudsystem.entity.DO.Setting;
import com.shiyq.cloudsystem.entity.DTO.UserContext;
import com.shiyq.cloudsystem.entity.VO.SettingVO;
import com.shiyq.cloudsystem.mapper.SettingMapper;
import com.shiyq.cloudsystem.service.SettingService;
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
    public SettingVO getSettingByUserId() {
        return SettingConvert.INSTANCE.settingDO2VO(settingMapper.selectById(UserContext.getCurrentUserId()));
    }

    /**
     * 更新设置
     * @param settingVO 前端传来的更新设置
     * @return 是否更新成功
     */
    @Override
    public boolean updateSetting(SettingVO settingVO) {
        return settingMapper.updateById(SettingConvert.INSTANCE.settingVO2DO(settingVO)) > 0;
    }
}
