package com.shiyq.imagecloud.convert;

import com.shiyq.imagecloud.entity.DO.Setting;
import com.shiyq.imagecloud.entity.DTO.SettingDTO;
import com.shiyq.imagecloud.entity.DTO.UserContext;
import com.shiyq.imagecloud.entity.VO.SettingVO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(uses = BooleanToStringFormat.class)
public abstract class SettingConvert {
    public static SettingConvert INSTANCE = Mappers.getMapper(SettingConvert.class);

    // SettingVO --> Setting
    public abstract Setting settingVOToSettingDO(SettingVO settingVO);
    @AfterMapping
    public void settingVOToSettingDOAfter(SettingVO settingVO, @MappingTarget Setting setting) {
        setting.setUserId(UserContext.getCurrentUserId());
    }

    // Setting --> SettingDTO
    public abstract SettingDTO settingToSettingDTO(Setting setting);
}
