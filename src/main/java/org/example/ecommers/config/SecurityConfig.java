package org.example.ecommers.config;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.security.CustomUserDetailsService;
import org.example.ecommers.security.JwtService;
import org.example.ecommers.security.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtService, customUserDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/products/**").permitAll()
                        .requestMatchers("/api/carts/**").permitAll()
                        .requestMatchers("/api/cart-items/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter(),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxMiIsInVzZXJuYW1lIjoicmFtaW4xMjMiLCJpYXQiOjE3NzMyNTIxMjcsImV4cCI6MTc3MzI1MzAyN30.AinYNO699tzSXW7TNfQGoO2kru07LQnBe3hOLi9PUXA5xTi4vMu-YDhc3s75xwgL
}

