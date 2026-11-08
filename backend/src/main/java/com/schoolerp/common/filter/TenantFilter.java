package com.schoolerp.common.filter;

import com.schoolerp.common.context.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantFilter extends OncePerRequestFilter {

    public static final String TENANT_HEADER = "X-Tenant-ID";
    public static final String DEFAULT_TENANT = "tenant_apex_mumbai_01";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String tenantId = request.getHeader(TENANT_HEADER);

        // Fallback: Check Host subdomain (e.g. apex.schoolerp.com)
        if (tenantId == null || tenantId.isBlank()) {
            String serverName = request.getServerName();
            if (serverName != null && serverName.contains(".")) {
                String sub = serverName.split("\\.")[0];
                if (!sub.equalsIgnoreCase("www") && !sub.equalsIgnoreCase("api") && !sub.equalsIgnoreCase("localhost")) {
                    tenantId = "tenant_" + sub;
                }
            }
        }

        // Local development default fallback
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = DEFAULT_TENANT;
        }

        TenantContext.setTenantId(tenantId);
        response.setHeader(TENANT_HEADER, tenantId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}

