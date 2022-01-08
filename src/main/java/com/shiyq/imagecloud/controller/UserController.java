package com.shiyq.imagecloud.controller;

import com.shiyq.imagecloud.constant.HttpStatus;
import com.shiyq.imagecloud.entity.DTO.UserDTO;
import com.shiyq.imagecloud.entity.DTO.XhrResult;
import com.shiyq.imagecloud.entity.VO.UserVO;
import com.shiyq.imagecloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
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
        UserDTO userDTO = userService.signIn(userVO);
        return userDTO != null
                ? XhrResult.success(userDTO)
                : XhrResult.error(HttpStatus.UNAUTHORIZED, "E-mail address / user id or password is incorrect!");
    }

    @PostMapping("/signup")
    public XhrResult signup(@RequestBody UserVO userVO) {
        UserDTO userDTO = userService.signup(userVO);
        return userDTO != null
                ? XhrResult.success(userDTO)
                : XhrResult.error(HttpStatus.UNAUTHORIZED, "Please enter your E-mail verification code!");
    }

    // TODO url携带email可能会出现特殊字符的问题（如：?）
    @GetMapping("/sendEmailVerificationCode/{email}")
    public XhrResult sendEmailVerificationCode(@PathVariable String email) {
        return userService.sendEmailVerificationCode(email) ? XhrResult.success() : XhrResult.error();
    }

}

