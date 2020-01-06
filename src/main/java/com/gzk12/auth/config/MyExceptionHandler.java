package com.gzk12.auth.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @Author Yang ShuNing
 * @Date 2020/1/3
 */
@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseBody
    public Object accessDenied(AccessDeniedException ex){
        return "没权限";
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Object methodNotSupport(HttpRequestMethodNotSupportedException ex){
        return "请求方法不支持";
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    @ResponseBody
    public Object usernameNotFound(UsernameNotFoundException ex){
        return "用户名或密码错误";
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseBody
    public Object badCredentials(BadCredentialsException ex){
        return "用户名或密码错误";
    }
}
