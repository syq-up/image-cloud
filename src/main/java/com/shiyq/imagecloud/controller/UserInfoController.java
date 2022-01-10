package com.shiyq.imagecloud.controller;

import com.shiyq.imagecloud.entity.DTO.XhrResult;
import com.shiyq.imagecloud.entity.VO.SecondaryPathVO;
import com.shiyq.imagecloud.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

}

