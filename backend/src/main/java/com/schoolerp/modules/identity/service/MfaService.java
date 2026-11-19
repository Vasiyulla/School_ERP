package com.schoolerp.modules.identity.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.common.security.TotpService;
import com.schoolerp.modules.identity.dto.MfaDTOs;
import com.schoolerp.modules.identity.entity.User;
import com.schoolerp.modules.identity.entity.UserMfaProfile;
import com.schoolerp.modules.identity.repository.UserMfaProfileRepository;
import com.schoolerp.modules.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MfaService {

    private final UserMfaProfileRepository userMfaProfileRepository;
    private final UserRepository userRepository;
    private final TotpService totpService;

    @Transactional
    public MfaDTOs.SetupResponse setupMfa(String email) {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = "apex";
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + email));

        String secret = totpService.generateBase32Secret();
        List<String> scratchCodes = totpService.generateScratchCodes(8);
        String scratchCodesStr = String.join(",", scratchCodes);
        String otpAuthUri = totpService.getOtpAuthUri(secret, email, "Apex School ERP");

        UserMfaProfile profile = userMfaProfileRepository.findByEmailAndTenantId(email, tenantId)
                .orElse(UserMfaProfile.builder()
                        .userId(user.getId())
                        .tenantId(tenantId)
                        .email(email)
                        .enabled(false)
                        .build());

        profile.setSecretKey(secret);
        profile.setBackupScratchCodes(scratchCodesStr);
        userMfaProfileRepository.save(profile);

        return MfaDTOs.SetupResponse.builder()
                .email(email)
                .secretKey(secret)
                .otpAuthUri(otpAuthUri)
                .recoveryScratchCodes(scratchCodes)
                .isEnabled(profile.isEnabled())
                .message("MFA setup generated. Scan the QR code or enter secret into Authenticator, then verify with 6-digit code.")
                .build();
    }

    @Transactional
    public MfaDTOs.VerifyResponse verifyMfa(String email, String code) {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = "apex";
        }

        UserMfaProfile profile = userMfaProfileRepository.findByEmailAndTenantId(email, tenantId)
                .or(() -> userMfaProfileRepository.findByEmail(email))
                .orElseThrow(() -> new NoSuchElementException("MFA profile not found for email: " + email));

        boolean codeValid = totpService.verifyTotp(profile.getSecretKey(), code);
        boolean isRecovery = false;

        if (!codeValid && profile.getBackupScratchCodes() != null) {
            // Check scratch recovery codes
            List<String> codes = new ArrayList<>(Arrays.asList(profile.getBackupScratchCodes().split(",")));
            if (codes.contains(code.trim())) {
                codes.remove(code.trim());
                profile.setBackupScratchCodes(String.join(",", codes));
                codeValid = true;
                isRecovery = true;
            }
        }

        if (!codeValid) {
            return MfaDTOs.VerifyResponse.builder()
                    .success(false)
                    .email(email)
                    .recoveryCodeUsed(false)
                    .message("Invalid MFA code or recovery token provided.")
                    .build();
        }

        profile.setEnabled(true);
        profile.setEnabledAt(Instant.now());
        profile.setLastVerifiedAt(Instant.now());
        userMfaProfileRepository.save(profile);

        return MfaDTOs.VerifyResponse.builder()
                .success(true)
                .email(email)
                .recoveryCodeUsed(isRecovery)
                .message(isRecovery ? "MFA verified successfully via one-time recovery scratch code." : "MFA verified successfully via 6-digit TOTP token.")
                .build();
    }
}
