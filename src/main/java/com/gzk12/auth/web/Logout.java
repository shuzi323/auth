package com.gzk12.auth.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class Logout {
    @GetMapping("logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "logout success";
    }
}
