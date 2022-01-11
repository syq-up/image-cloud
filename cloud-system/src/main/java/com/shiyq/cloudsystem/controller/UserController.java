package com.shiyq.cloudsystem.controller;

import com.shiyq.cloudsystem.constant.HttpStatus;
import com.shiyq.cloudsystem.entity.VO.UserVO;
import com.shiyq.cloudsystem.entity.VO.XhrResult;
import com.shiyq.cloudsystem.entity.VO.UserRequest;
import com.shiyq.cloudsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-in")
    public XhrResult signIn(@RequestBody UserRequest userRequest) {
        UserVO userDTO = userService.signIn(userRequest);
        return userDTO != null
                ? XhrResult.success(userDTO)
                : XhrResult.error(HttpStatus.UNAUTHORIZED, "E-mail address or user id and password are incorrect!");
    }

    @PostMapping("/signup")
    public XhrResult signup(@RequestBody UserRequest userRequest) {
        UserVO userDTO = userService.signup(userRequest);
        return userDTO != null
                ? XhrResult.success(userDTO)
                : XhrResult.error(HttpStatus.UNAUTHORIZED, "E-mail verification code error!");
    }

    @PostMapping("/checkSameUsername")
    public XhrResult checkSameUsername(@RequestBody UserRequest userRequest) {
        return userService.checkSameUsername(userRequest.getUsername())
                ? XhrResult.error(HttpStatus.CONFLICT, "The current E-mail address is already registered!")
                : XhrResult.success();
    }

    @PostMapping("/sendEmailVerificationCode")
    public XhrResult sendEmailVerificationCode(@RequestBody UserRequest userRequest) {
        return userService.sendEmailVerificationCode(userRequest.getUsername())
                ? XhrResult.success()
                : XhrResult.error();
    }

}

