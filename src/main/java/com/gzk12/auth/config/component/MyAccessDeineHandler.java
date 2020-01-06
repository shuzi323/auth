package com.gzk12.auth.config.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 访问没有权限的接口时的返回
 * @Author Yang ShuNing
 * @Date 2020/1/3
 */
public class MyAccessDeineHandler implements AccessDeniedHandler {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, Object> notLogin = new HashMap<>();
    static {
        notLogin.put("code", 403);
        notLogin.put("msg", "没有权限");
    }

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Cache-Control", "no-cache, must-revalidate");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(notLogin));
    }
}
