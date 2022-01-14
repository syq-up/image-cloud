package com.shiyq.cloudsystem.controller;

import com.shiyq.cloudsystem.entity.VO.XhrResult;
import com.shiyq.cloudsystem.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}

