package com.schoolerp.config;

import com.schoolerp.common.filter.TenantFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TenantFilter tenantFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/h2-console/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Protected endpoints with role checking
                        .requestMatchers("/api/v1/system/**").hasRole("SUPER_ADMIN")
                        .requestMatchers("/api/v1/finance/**").hasAnyRole("SUPER_ADMIN", "ACCOUNTANT")
                        .requestMatchers("/api/v1/attendance/**").hasAnyRole("SUPER_ADMIN", "TEACHER", "PRINCIPAL")
                        .requestMatchers("/api/v1/examinations/**").hasAnyRole("SUPER_ADMIN", "TEACHER", "PRINCIPAL")
                        .requestMatchers("/api/v1/students/**").hasAnyRole("SUPER_ADMIN", "TEACHER", "PRINCIPAL", "ACCOUNTANT")
                        .requestMatchers("/api/v1/admissions/**").hasAnyRole("SUPER_ADMIN", "PRINCIPAL")
                        .requestMatchers("/api/v1/academics/**").hasAnyRole("SUPER_ADMIN", "TEACHER", "PRINCIPAL")
                        .requestMatchers("/api/v1/staff/**").hasAnyRole("SUPER_ADMIN", "TEACHER", "PRINCIPAL")
                        .requestMatchers("/api/v1/operations/**").hasAnyRole("SUPER_ADMIN", "TEACHER", "PRINCIPAL", "ACCOUNTANT")
                        .anyRequest().authenticated()
                )
                // Allow H2 console frames for dev
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .addFilterBefore(tenantFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:3001"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Tenant-ID", "X-Idempotency-Key"));
        config.setExposedHeaders(List.of("X-Tenant-ID"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
