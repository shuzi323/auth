package com.gzk12.auth.service;

/**
 * @Author Yang ShuNing
 * @Date 2019/12/26
 */
public interface SecurityService {
    String findLoggedUsername();

    void autoLogin(String username, String password);
}
