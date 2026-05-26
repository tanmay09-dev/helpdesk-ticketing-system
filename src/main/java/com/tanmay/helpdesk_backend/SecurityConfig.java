package com.tanmay.helpdesk_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // PUBLIC APIs
                .requestMatchers(
                        "/hello",
                        "/auth/signup",
                        "/auth/login"
                ).permitAll()

                .requestMatchers(HttpMethod.GET,
                           "/users/**")
                        .hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE,
                            "/users/**")
                        .hasRole("ADMIN")

                .requestMatchers(HttpMethod.PUT,
                            "/users/**")
                        .hasAnyRole("USER", "ADMIN")

                .requestMatchers(HttpMethod.POST,
                              "/tickets")
                       .hasAnyRole("USER", "ADMIN")

                .requestMatchers(HttpMethod.DELETE,
                           "/tickets/**")
                        .hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET,
                            "/tickets/**")
                        .hasAnyRole("USER", "ADMIN")

                .requestMatchers(HttpMethod.PUT,
                           "/tickets/user/**")
                        .hasAnyRole("USER", "ADMIN")

                .requestMatchers(HttpMethod.PUT,
                          "/tickets/admin/**")
                        .hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST,
                           "/comments/**")
                       .hasAnyRole("USER", "ADMIN")  
                       
                .requestMatchers(HttpMethod.GET,
                           "/comments/**")
                        .hasAnyRole("USER", "ADMIN")       

                // PROTECTED APIs
                .anyRequest().authenticated()
            )

            .addFilterBefore(
                    jwtFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}