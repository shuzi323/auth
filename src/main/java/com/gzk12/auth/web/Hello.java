package com.gzk12.auth.web;

import com.gzk12.auth.annotation.LoginUser;
import com.gzk12.auth.model.User;
import com.gzk12.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

/**
 * @Author Yang ShuNing
 * @Date 2019/12/26
 */
@RestController
@RequestMapping("test")
public class Hello {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FindByIndexNameSessionRepository<? extends Session> findByIndexNameSessionRepository;

    @GetMapping("hello")
    public List<User> hello(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit){
        return userRepository.findAllByDeletedFalse();
    }

    @GetMapping("hello2")
    public Page<User> hello2(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit){
        return userRepository.findAll(PageRequest.of(page, limit));
    }

    @GetMapping("user")
    public Object user(Principal principal){
        System.out.println(principal);
        if (principal instanceof UserDetails){
            System.out.println("直接是 UserDetails：");
            System.out.println((UserDetails) principal);
        }else if (principal instanceof UsernamePasswordAuthenticationToken){
            Object principal2 = ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            if (principal2 instanceof UserDetails){
                System.out.println("从 UsernamePasswordAuthenticationToken 获取: ");
                System.out.println(principal2);
            }
        }

        return principal;
    }

    @GetMapping("user2")
    public UserDetails user2(@LoginUser UserDetails userDetails){
        System.out.println(userDetails);
        return userDetails;
    }

    @GetMapping("session")
    public Object session(Principal principal, HttpServletRequest request){
        System.out.println(request.getSession().getId());
        if (principal instanceof UsernamePasswordAuthenticationToken){
            return ((UsernamePasswordAuthenticationToken) principal).getDetails();
        }else if (principal instanceof UserDetails){
            return principal;
        }
        System.out.println(principal);
        return null;
    }

    @GetMapping("getSessionByUser")
    public Object getSessionByUser(@LoginUser UserDetails userDetails){
        return findByIndexNameSessionRepository
                .findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, userDetails.getUsername());
    }
}
