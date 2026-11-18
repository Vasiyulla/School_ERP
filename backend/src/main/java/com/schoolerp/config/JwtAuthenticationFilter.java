package com.schoolerp.config;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.identity.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwtService.validateToken(jwt)) {
                Claims claims = jwtService.extractClaims(jwt);
                String email = claims.getSubject();
                String tokenTenantId = (String) claims.get("tenantId");

                // Strictly enforce tenant isolation: Token tenant must match requested tenant header
                if (tokenTenantId != null) {
                    String requestedTenant = request.getHeader("X-Tenant-ID");
                    if (requestedTenant != null && !requestedTenant.isBlank() && !requestedTenant.equalsIgnoreCase(tokenTenantId)) {
                        log.warn("Security Alert: Cross-tenant access attempt blocked. Token tenant='{}', Requested tenant='{}'",
                                tokenTenantId, requestedTenant);
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"success\":false,\"message\":\"Access denied: Token tenant does not match requested tenant context.\"}");
                        return;
                    }
                    TenantContext.setTenantId(tokenTenantId);
                }

                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) claims.get("roles");
                List<SimpleGrantedAuthority> authorities = roles == null ? List.of() :
                        roles.stream()
                                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}

