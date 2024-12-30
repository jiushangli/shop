package com.shop.user_service.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import common.annotation.UserId;
import com.shop.user_service.config.TokenConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@ResponseBody
@RequestMapping("/user")
public class UserController {

    @Autowired
    private TokenConfig tokenConfig;

    //登录 先获取token
    @RequestMapping("/login")
    public String Login(Long userId){
        log.info("login");
        String token = JWT.create()
                .withClaim("userId", userId)
                .sign(Algorithm.HMAC256(tokenConfig.tokenKy));
        return token;
    }

    //测试使用账户注解
    @GetMapping("/auth/userInfo")
    public Long getUserInfo(@UserId Long userId){
        return userId;
    }

}
