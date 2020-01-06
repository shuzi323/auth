package com.gzk12.auth.service;

import com.gzk12.auth.model.User;

/**
 * @Author Yang ShuNing
 * @Date 2019/12/26
 */
public interface UserService {
    void save(User user);
    User findByUsername(String username);
}
