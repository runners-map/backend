package com.service.runnersmap.common.config;

import com.service.runnersmap.auth.handler.OAuthLoginFailureHandler;
import com.service.runnersmap.auth.handler.OAuthLoginSuccessHandler;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final OAuthLoginSuccessHandler oAuthLoginSuccessHandler;
  private final OAuthLoginFailureHandler oAuthLoginFailureHandler;

  // CORS 설정
  CorsConfigurationSource corsConfigurationSource() {
    return request -> {
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowedHeaders(Collections.singletonList("*"));
      config.setAllowedMethods(Collections.singletonList("*"));
      config.setAllowedOriginPatterns(Collections.singletonList("*"));
      config.setAllowCredentials(true);
      return config;
    };
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.
        httpBasic(HttpBasicConfigurer::disable)
        .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize ->
            authorize
                .requestMatchers("/**").permitAll()
        )

        .oauth2Login(oauth ->
            oauth
                .successHandler(oAuthLoginSuccessHandler) // 로그인 성공 시 핸들러
                .failureHandler(oAuthLoginFailureHandler) // 로그인 실패 시 핸들러
        );

    return httpSecurity.build();
  }
}