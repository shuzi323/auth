package com.gzk12.auth.config;

import com.gzk12.auth.config.component.MyAuthenticationFailureHandler;
import com.gzk12.auth.config.component.MyAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.SimpleRedirectInvalidSessionStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @Author Yang ShuNing
 * @Date 2020/1/3
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig<S extends Session> extends WebSecurityConfigurerAdapter {
    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Resource
    private @NotNull FindByIndexNameSessionRepository<S> findByIndexNameSessionRepository;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/session/invalid", "/commonError/expireSession",
                        "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler)
                .and()
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .httpBasic().disable()
                .requestCache().disable();
//                .requestCache().requestCache(new NullRequestCache());
        http
                .authorizeRequests().antMatchers("/registration", "/test/hello", "/auth/login").permitAll()
                .antMatchers("/test/getSessionByUser").hasRole("USER")
                .anyRequest().authenticated();
        http
                .sessionManagement().invalidSessionStrategy(invalidSessionStrategy()).maximumSessions(1).expiredUrl("/commonError/expireSession").sessionRegistry(sessionRegistry());
    }

    /**
     * 控制并发session
     * @return
     */
    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy(){
        ConcurrentSessionControlAuthenticationStrategy strategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        strategy.setMaximumSessions(1);
        strategy.setExceptionIfMaximumExceeded(false);
        return strategy;
    }

    /**
     * 通过 UsernamePasswordAuthenticationFilter 自定义登录路由
     * 可以重写这个类来实现自己的登录
     * @return
     * @throws Exception
     */
    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception{
        UsernamePasswordAuthenticationFilter authenticationFilter = new UsernamePasswordAuthenticationFilter();
        authenticationFilter.setPostOnly(true);
        authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/auth/login", "POST"));
        authenticationFilter.setAuthenticationManager(customAuthenticationManager());
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        authenticationFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return authenticationFilter;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new MyAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
       return new MyAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    /**
     * 设置userDetailsService 以及密码编码方式
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, BCryptPasswordEncoder passwordEncoder) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     *
     * @return
     */
    @Bean
    public SpringSessionBackedSessionRegistry<S> sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.findByIndexNameSessionRepository);
    }

    /**
     * 设置session失效重定向，设置不生成新session
     * @return
     */
    @Bean
    public SimpleRedirectInvalidSessionStrategy invalidSessionStrategy(){
        SimpleRedirectInvalidSessionStrategy sessionStrategy = new SimpleRedirectInvalidSessionStrategy("/session/invalid");
        sessionStrategy.setCreateNewSession(false);
        return sessionStrategy;
    }
}
