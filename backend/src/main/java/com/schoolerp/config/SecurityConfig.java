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
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
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
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
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
                                "/api/v1/finance/webhooks/**",
                                "/api/v1/examinations/gradebook/report-card/verify/**",
                                "/api/v1/students/transfer-certificates/verify/**",
                                "/api/v1/students/certificates/verify/**",
                                "/api/v1/staff/payroll/biometric/punch",
                                "/api/v1/attendance/biometric/**",
                                "/api/v1/operations/telemetry/**",
                                "/api/v1/operations/gate-passes/verify-scan",
                                "/api/v1/admissions/crm/portal/**",
                                "/api/v1/admissions/crm/entrance-tests/admit-card/**",
                                "/h2-console/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/actuator/health",
                                "/actuator/info",
                                "/actuator/prometheus"
                        ).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Hardened Actuator Access (Only Super Admin can access full actuator telemetry)
                        .requestMatchers("/actuator/**").hasRole("SUPER_ADMIN")
                        // Mobile Parent/Student API requires valid authentication
                        .requestMatchers("/api/v1/mobile/**").authenticated()
                        // Protected endpoints with role checking aligned with method-level security
                        .requestMatchers("/api/v1/system/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "PRINCIPAL", "TEACHER", "STAFF", "ACCOUNTANT")
                        .requestMatchers("/api/v1/communication/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "TEACHER", "PRINCIPAL", "STAFF", "PARENT", "STUDENT")
                        .requestMatchers("/api/v1/finance/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "PRINCIPAL", "ACCOUNTANT", "FRONT_DESK", "ADMISSION_OFFICER")
                        .requestMatchers("/api/v1/attendance/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "TEACHER", "PRINCIPAL", "STAFF")
                        .requestMatchers("/api/v1/examinations/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "TEACHER", "PRINCIPAL", "STAFF")
                        .requestMatchers("/api/v1/students/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "TEACHER", "PRINCIPAL", "ACCOUNTANT", "STAFF", "PARENT", "STUDENT")
                        .requestMatchers("/api/v1/admissions/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "PRINCIPAL", "ADMISSION_OFFICER", "FRONT_DESK", "ACCOUNTANT")
                        .requestMatchers("/api/v1/academics/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "TEACHER", "PRINCIPAL", "STAFF", "STUDENT")
                        .requestMatchers("/api/v1/staff/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "TEACHER", "PRINCIPAL", "STAFF", "ACCOUNTANT")
                        .requestMatchers("/api/v1/operations/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "TEACHER", "PRINCIPAL", "ACCOUNTANT", "FRONT_DESK", "STAFF")
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
