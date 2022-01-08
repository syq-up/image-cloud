package com.shiyq.imagecloud.controller;

import com.shiyq.imagecloud.entity.DTO.XhrResult;
import com.shiyq.imagecloud.entity.VO.SecondaryPathVO;
import com.shiyq.imagecloud.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @param id 用户id
     * @return 返回结果
     */
    @GetMapping("/getUserInfo/{id}")
    public XhrResult getUserInfo(@PathVariable long id) {
        return XhrResult.success(userInfoService.getUserInfo(id));
    }

    /**
     * 更新次级路径信息
     * @param secondaryPathVO 新的次级路径信息
     * @return 返回结果
     */
    @PostMapping("/updateSecondaryPath")
    public XhrResult updateSecondaryPath(@RequestBody SecondaryPathVO secondaryPathVO) {
        return XhrResult.success(userInfoService.updateUserInfo(secondaryPathVO));
    }

}

