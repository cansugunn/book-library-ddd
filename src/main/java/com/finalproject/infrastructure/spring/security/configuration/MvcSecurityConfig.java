package com.finalproject.infrastructure.spring.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Order(2)
@Configuration
@EnableWebSecurity
public class MvcSecurityConfig {
    @Bean
    public SecurityFilterChain mvcSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/mvc/**")
                .csrf().and()
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/",
                                "/mvc/media/cover",
                                "/mvc/books",
                                "/mvc/login",
                                "/resources/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(form -> form
                        .loginPage("/mvc/login")
                        .loginProcessingUrl("/mvc/login")
                        .defaultSuccessUrl("/mvc/books", true)
                        .failureUrl("/mvc/login?error"))
                .logout(logout -> logout
                        .logoutUrl("/mvc/logout")
                        .logoutSuccessUrl("/mvc/login?logout"));

        return http.build();
    }
}
