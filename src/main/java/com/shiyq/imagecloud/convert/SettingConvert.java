package com.shiyq.imagecloud.convert;

import com.shiyq.imagecloud.entity.DO.Setting;
import com.shiyq.imagecloud.entity.DTO.SettingDTO;
import com.shiyq.imagecloud.entity.VO.SettingVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = BooleanToStringFormat.class)
public abstract class SettingConvert {
    public static SettingConvert INSTANCE = Mappers.getMapper(SettingConvert.class);

    // SettingVO --> Setting
    public abstract Setting settingVOToSettingDO(SettingVO settingVO);

    // Setting --> SettingDTO
    public abstract SettingDTO settingToSettingDTO(Setting setting);
}
