package com.sellit.sellit.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    @Bean("passwordEncoder")
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder(10);
    }
}
