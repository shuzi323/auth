package com.gzk12.auth.annotation.support;

import com.gzk12.auth.annotation.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;


@Slf4j
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserDetails.class) && parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        Principal principal = request.getUserPrincipal();
        if (principal instanceof UsernamePasswordAuthenticationToken){
            Object principal2 = ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            if (principal2 instanceof UserDetails){
                return principal2;
            }
        }else if (principal instanceof UserDetails){
            return principal;
        }
        return null;
    }
}
