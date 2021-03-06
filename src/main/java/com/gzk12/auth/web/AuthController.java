package com.gzk12.auth.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzk12.auth.service.SecurityService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Yang ShuNing
 * @Date 2019/12/31
 */
@RestController
@RequestMapping("auth")
@Slf4j
public class AuthController {
    @Autowired
    private SecurityService securityService;

//    @PostMapping("login")
//    public Object login(String username, String password, HttpSession session){
//        log.info("登录：{}", username);
//        securityService.autoLogin(username, password);
//        Map<String, String> data = new HashMap<>();
//        data.put("msg", "登录成功");
//        data.put("sessionId", session.getId());
//        return data;
//    }

    @ApiOperation(value = "退出登录接口")
    @GetMapping("logout")
    public Object logout(HttpSession session){
        session.invalidate();
//        SecurityContextHolder.clearContext();
        return "logout success";
    }
}
