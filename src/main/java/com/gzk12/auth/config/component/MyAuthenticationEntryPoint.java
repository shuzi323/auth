package com.gzk12.auth.config.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 未登录的返回
 * @Author Yang ShuNing
 * @Date 2020/1/3
 */
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, Object> notLogin = new HashMap<>();
    static {
        notLogin.put("code", 502);
        notLogin.put("msg", "未登录");
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Cache-Control", "no-cache, must-revalidate");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(notLogin));
    }
}
