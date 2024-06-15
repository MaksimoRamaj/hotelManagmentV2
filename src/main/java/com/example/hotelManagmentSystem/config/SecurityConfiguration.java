package com.example.hotelManagmentSystem.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(request ->
                                new CorsConfiguration().applyPermitDefaultValues()
                        ))
                .csrf((httpSecurityCsrfConfigurer)->httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,"/api/**")
                        .hasAnyAuthority("ADMIN","USER")
                        .requestMatchers(HttpMethod.DELETE)
                        .hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT)
                        .hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH)
                        .hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"api/acmd/**")
                        .hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/hotel/available","/api/hotel/available","api/room/reserve")
                        .hasAnyAuthority("ADMIN","USER")
                        .requestMatchers("api/auth/**")
                        .permitAll()
                        .requestMatchers("/v3/**", "/swagger-ui/**", "/swagger-ui.html","swagger-ui/swagger-ui/index.html")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(sesion->sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter,UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}