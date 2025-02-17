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
                        // PERMITIR TODOS LOS GET en los 4 controladores
                        .requestMatchers(HttpMethod.GET,
                                "/agency/hotels/**",
                                "/agency/flights/**",
                                "/agency/flight-booking/**",
                                "/agency/room-booking/**").permitAll()

                        // SOLO EMPLEADOS AUTENTICADOS PUEDEN HACER POST, PUT Y DELETE en los 4 controladores
                        .requestMatchers(HttpMethod.POST,
                                "/agency/hotels/new",
                                "/agency/flights/new",
                                "/agency/flight-booking/new",
                                "/agency/room-booking/new").authenticated()

                        .requestMatchers(HttpMethod.PUT,
                                "/agency/hotels/edit/**",
                                "/agency/flights/edit/**").authenticated()

                        .requestMatchers(HttpMethod.DELETE,
                                "/agency/hotels/delete/**",
                                "/agency/flights/delete/**").authenticated()

                        // OTRAS SOLICITUDES REQUIEREN AUTENTICACIÓN
                        .anyRequest().authenticated()
                )

                // Habilita autenticación básica (para pruebas; en producción, usa JWT)
                .httpBasic(Customizer.withDefaults())

                // Construye la cadena de seguridad
                .build();
    }


}
