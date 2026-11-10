package com.schoolerp.modules.identity.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.identity.dto.AuthResponse;
import com.schoolerp.modules.identity.dto.LoginRequest;
import com.schoolerp.modules.identity.entity.Role;
import com.schoolerp.modules.identity.entity.User;
import com.schoolerp.modules.identity.repository.UserRepository;
import com.schoolerp.modules.institution.entity.Tenant;
import com.schoolerp.modules.institution.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SessionSecurityService sessionSecurityService;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = "tenant_apex_mumbai_01";
        }

        // 1. Find user in the tenant context
        User user = userRepository.findByEmailAndTenantId(request.getEmail(), tenantId)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        // 2. Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        // 3. Resolve Tenant Information
        Tenant tenant = tenantRepository.findByTenantIdentifier(tenantId)
                .orElse(Tenant.builder()
                        .tenantIdentifier(tenantId)
                        .schoolName("Apex Academy (Mumbai)")
                        .subdomain("apex")
                        .affiliationNumber("CBSE #113042")
                        .build());

        // 4. Generate JWT with tenant claims + tracked refresh session
        String token = jwtService.generateToken(user, tenantId);
        String refreshToken = sessionSecurityService.issueRefreshSession(user, tenantId, "Web/Mobile Portal", "127.0.0.1");

        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .user(AuthResponse.UserDto.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .roles(roleNames)
                        .build())
                .tenant(AuthResponse.TenantDto.builder()
                        .tenantIdentifier(tenant.getTenantIdentifier())
                        .schoolName(tenant.getSchoolName())
                        .subdomain(tenant.getSubdomain())
                        .affiliationNumber(tenant.getAffiliationNumber())
                        .build())
                .build();
    }
}
