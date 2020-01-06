package com.gzk12.auth.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Yang ShuNing
 * @Date 2019/12/30
 */
@RestController
public class Session {
    @GetMapping("session/invalid")
    public String invalid(){
        return "session 过期";
    }
}
