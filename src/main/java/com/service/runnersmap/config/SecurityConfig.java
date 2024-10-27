package com.service.runnersmap.config;

import com.service.runnersmap.component.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter)
      throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
        // HTTP 요청에 대한 권한
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/api/user/sign-up",
                "/api/user/login",
                "/api/user/refresh"
            ).permitAll()
            .anyRequest().authenticated()
        )
        // 폼 로그인 기능을 비활성화
        .formLogin(form -> form.disable())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // jwt 필터 추가
    return http.build();
  }

}