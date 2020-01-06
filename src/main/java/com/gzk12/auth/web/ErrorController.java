package com.gzk12.auth.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Yang ShuNing
 * @Date 2020/1/6
 */
@RestController
@RequestMapping("commonError")
public class ErrorController {
    @GetMapping("expireSession")
    public Object expireSession(){
        return "多用户登录，您已被下线";
    }
}
