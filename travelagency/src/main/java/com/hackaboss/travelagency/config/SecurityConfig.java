package com.hackaboss.travelagency.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfig {

    // Configuración de seguridad para permitir todas las solicitudes
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Deshabilita la protección CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // Configura las reglas de autorización de solicitudes
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                //Permitir solicitures get y options
                                .requestMatchers(HttpMethod.GET).permitAll()
                                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                .requestMatchers(HttpMethod.POST).permitAll()
                                .requestMatchers(HttpMethod.PUT).permitAll()
                                .requestMatchers(HttpMethod.DELETE).permitAll())

                // Construye la cadena de filtros de seguridad
                .build();
    }

}
