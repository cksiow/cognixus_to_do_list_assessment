package com.cognixus.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {
    /**
     * Use for auth's pwd encode
     *
     * @return BCryptPasswordEncoder
     */

    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
