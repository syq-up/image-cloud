package com.shiyq.cloudsystem.controller;

import com.shiyq.cloudsystem.entity.VO.XhrResult;
import com.shiyq.cloudsystem.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shiyq
 * @since 2022-01-05
 */
@RestController
@RequestMapping("/user-info")
public class UserInfoController {

    private UserInfoService userInfoService;

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/getUserInfo")
    public XhrResult getUserInfo() {
        return XhrResult.success(userInfoService.getUserInfo());
    }

    @GetMapping("/updateUserInfo")
    public XhrResult updateUserInfo(@RequestParam String nickname) {
        return userInfoService.updateUserInfo(nickname) >= 0
                ? XhrResult.success("Nickname changed successfully.")
                : XhrResult.error();
    }

    @PostMapping("/uploadAvatar")
    public XhrResult uploadAvatar(MultipartFile avatarFile) throws Exception {
        XhrResult xhrResult = XhrResult.success();
        xhrResult.put("avatarUrl", userInfoService.uploadUserAvatar(avatarFile));
        return xhrResult;
    }

}

