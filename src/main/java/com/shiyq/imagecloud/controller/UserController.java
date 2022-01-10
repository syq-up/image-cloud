package com.shiyq.imagecloud.controller;

import com.shiyq.imagecloud.constant.HttpStatus;
import com.shiyq.imagecloud.entity.DTO.UserTokenDTO;
import com.shiyq.imagecloud.entity.DTO.XhrResult;
import com.shiyq.imagecloud.entity.VO.UserVO;
import com.shiyq.imagecloud.service.UserService;
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
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-in")
    public XhrResult signIn(@RequestBody UserVO userVO) {
        UserTokenDTO userDTO = userService.signIn(userVO);
        return userDTO != null
                ? XhrResult.success(userDTO)
                : XhrResult.error(HttpStatus.UNAUTHORIZED, "E-mail address or user id and password are incorrect!");
    }

    @PostMapping("/signup")
    public XhrResult signup(@RequestBody UserVO userVO) {
        UserTokenDTO userDTO = userService.signup(userVO);
        return userDTO != null
                ? XhrResult.success(userDTO)
                : XhrResult.error(HttpStatus.UNAUTHORIZED, "E-mail verification code error!");
    }

    @PostMapping("/checkSameUsername")
    public XhrResult checkSameUsername(@RequestBody UserVO userVO) {
        return userService.checkSameUsername(userVO.getUsername())
                ? XhrResult.error(HttpStatus.CONFLICT, "The current E-mail address is already registered!")
                : XhrResult.success();
    }

    @PostMapping("/sendEmailVerificationCode")
    public XhrResult sendEmailVerificationCode(@RequestBody UserVO userVO) {
        return userService.sendEmailVerificationCode(userVO.getUsername())
                ? XhrResult.success()
                : XhrResult.error();
    }

}

