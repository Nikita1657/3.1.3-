package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final AuthenticationSuccessHandler successUserHandler;

    @Autowired
    public WebSecurityConfig(AuthenticationSuccessHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/error").permitAll()
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/process_login")
                        .usernameParameter("email") // Используем email вместо username
                        .successHandler(successUserHandler)
                        .failureUrl("/login?error")
                        .permitAll()
                )


            ; // Отключаем CSRF для примера, рекомендуется оставить включенным
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}