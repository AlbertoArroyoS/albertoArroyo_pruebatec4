package com.hackaboss.travelagency.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Configuración de seguridad para permitir todas las solicitudes

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Deshabilita CSRF (en caso de no usar formularios HTML)
                .csrf(csrf -> csrf.disable())

                // Configura reglas de autorización
                .authorizeHttpRequests(auth -> auth
                        // Permitir acceso sin autenticación a Swagger UI y API Docs
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**").permitAll()

                        // Permitir todos los GET sin autenticación
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()

                        // Requerir autenticación para POST, PUT y DELETE
                        .requestMatchers(HttpMethod.POST, "/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/**").authenticated()

                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated()
                )

                // Habilita autenticación básica
                .httpBasic(Customizer.withDefaults())

                // Construye la cadena de seguridad
                .build();
    }



}
