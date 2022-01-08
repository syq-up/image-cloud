package com.shiyq.imagecloud.controller;

import com.shiyq.imagecloud.entity.DTO.XhrResult;
import com.shiyq.imagecloud.entity.VO.SettingVO;
import com.shiyq.imagecloud.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户的设置表，包含一些布局设置、上传设置等。 前端控制器
 * </p>
 *
 * @author shiyq
 * @since 2022-01-04
 */
@RestController
@RequestMapping("/setting")
public class SettingController {

    private SettingService settingService;

    @Autowired
    public void setSettingService(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping("/getSetting/{userId}")
    public XhrResult getSetting(@PathVariable long userId) {
        return XhrResult.success(settingService.getSettingByUserId(userId));
    }

    @PostMapping("/updateSetting")
    public XhrResult updateSetting(@RequestBody SettingVO settingVO) {
        return settingService.updateSetting(settingVO)
                ? XhrResult.success("ok")
                : XhrResult.error("error");
    }

}

