package com.gzk12.auth.config;

import com.gzk12.auth.config.component.MyAccessDeineHandler;
import com.gzk12.auth.config.component.MyAuthenticationEntryPoint;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.session.SimpleRedirectInvalidSessionStrategy;
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

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/session/invalid");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint()).accessDeniedHandler(new MyAccessDeineHandler())
                .and()
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .httpBasic().disable()
                .requestCache().requestCache(new NullRequestCache())
                .and()
                .authorizeRequests().antMatchers("/registration", "/test/hello", "/auth/login").permitAll()
                .antMatchers("/test/getSessionByUser").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .sessionManagement().invalidSessionStrategy(invalidSessionStrategy()).maximumSessions(1).maxSessionsPreventsLogin(false).sessionRegistry(sessionRegistry());
    }

//    @Bean
//    public SessionAuthenticationStrategy sessionAuthenticationStrategy(){
//        ConcurrentSessionControlAuthenticationStrategy strategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
//        strategy.setMaximumSessions(1);
//        strategy.setExceptionIfMaximumExceeded(false);
//        return strategy;
//    }

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
