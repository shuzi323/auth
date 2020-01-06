package com.gzk12.auth.config.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Yang ShuNing
 * @Date 2020/1/6
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final Map<String, Object> loginSuccess = new HashMap<>();
    static {
        loginSuccess.put("code", 0);
        loginSuccess.put("msg", "登录成功");
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Cache-Control", "no-cache, must-revalidate");
        loginSuccess.put("sessionId", httpServletRequest.getSession().getId());
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(loginSuccess));
    }
}
