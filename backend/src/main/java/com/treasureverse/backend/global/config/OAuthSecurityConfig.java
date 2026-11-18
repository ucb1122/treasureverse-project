package com.treasureverse.backend.global.config;

import com.treasureverse.backend.auth.infrastructure.JwtAuthenticationFilter;
import com.treasureverse.backend.auth.presentation.OAuth2LoginFailureHandler;
import com.treasureverse.backend.auth.presentation.OAuth2LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Profile("oauth")
public class OAuthSecurityConfig {
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      JwtAuthenticationFilter jwtFilter,
      OAuth2LoginSuccessHandler successHandler,
      OAuth2LoginFailureHandler failureHandler)
      throws Exception {
    return http.csrf(csrf -> csrf.disable())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .authorizeHttpRequests(
            authorization ->
                authorization
                    .requestMatchers(
                        "/api/v1/auth/**",
                        "/api/v1/health",
                        "/actuator/health/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/oauth2/**",
                        "/login/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/sale-posts", "/api/v1/sale-posts/**")
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.GET, "/api/v1/purchase-posts", "/api/v1/purchase-posts/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2Login(oauth -> oauth.successHandler(successHandler).failureHandler(failureHandler))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
