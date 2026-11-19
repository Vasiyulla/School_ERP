package com.schoolerp.modules.identity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class MfaDTOs {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetupResponse {
        private String email;
        private String secretKey;
        private String otpAuthUri;
        private List<String> recoveryScratchCodes;
        private boolean isEnabled;
        private String message;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerifyRequest {
        private String email;
        private String code; // 6-digit TOTP or recovery scratch code
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerifyResponse {
        private boolean success;
        private String email;
        private String message;
        private boolean recoveryCodeUsed;
    }
}
