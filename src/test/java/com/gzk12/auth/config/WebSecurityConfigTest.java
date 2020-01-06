package com.gzk12.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class WebSecurityConfigTest {

    @Test
    void bCryptPasswordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedStr = encoder.encode("1747882365@qq.com");
        log.debug("bCryptPasswordEncoder: {}", encodedStr);
        log.debug("{}", encoder.encode("12345678"));
        assertTrue(encoder.matches("1747882365@qq.com", encodedStr));
    }
}