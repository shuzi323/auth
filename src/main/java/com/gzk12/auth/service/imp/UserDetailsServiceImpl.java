package com.gzk12.auth.service.imp;

import com.gzk12.auth.DTO.CustomUserDetails;
import com.gzk12.auth.model.Role;
import com.gzk12.auth.model.User;
import com.gzk12.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author Yang ShuNing
 * @Date 2019/12/26
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * 获取UserDetail的实现，配合CustomUserDetails来将自定义的用户信息放到session中
     * @param username
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), grantedAuthorities);
        customUserDetails.setUserId(user.getId());
        log.info("用户信息：{}", customUserDetails);
        return customUserDetails;
    }
}
