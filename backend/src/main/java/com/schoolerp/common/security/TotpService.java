package com.schoolerp.common.security;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Component
public class TotpService {

    private static final String BASE32_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    private static final int SECRET_LENGTH = 16;
    private static final int TIME_STEP_SECONDS = 30;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public String generateBase32Secret() {
        StringBuilder sb = new StringBuilder(SECRET_LENGTH);
        for (int i = 0; i < SECRET_LENGTH; i++) {
            sb.append(BASE32_CHARS.charAt(SECURE_RANDOM.nextInt(BASE32_CHARS.length())));
        }
        return sb.toString();
    }

    public List<String> generateScratchCodes(int count) {
        List<String> codes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            codes.add(String.format("%04x-%04x", SECURE_RANDOM.nextInt(0x10000), SECURE_RANDOM.nextInt(0x10000)));
        }
        return codes;
    }

    public String getOtpAuthUri(String secret, String email, String issuer) {
        return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30",
                issuer.replace(" ", "%20"),
                email,
                secret,
                issuer.replace(" ", "%20"));
    }

    public boolean verifyTotp(String base32Secret, String codeStr) {
        if (base32Secret == null || codeStr == null) return false;
        String cleanCode = codeStr.trim();
        if (cleanCode.length() != 6 && !cleanCode.matches("\\d{6}")) {
            return false;
        }

        long currentStep = (System.currentTimeMillis() / 1000L) / TIME_STEP_SECONDS;
        byte[] keyBytes = decodeBase32(base32Secret);

        // Check clock drift tolerance of +/- 1 step
        for (int i = -1; i <= 1; i++) {
            String expected = generateTotpForStep(keyBytes, currentStep + i);
            if (expected.equals(cleanCode)) {
                return true;
            }
        }
        return false;
    }

    public String generateCurrentTotp(String base32Secret) {
        long currentStep = (System.currentTimeMillis() / 1000L) / TIME_STEP_SECONDS;
        byte[] keyBytes = decodeBase32(base32Secret);
        return generateTotpForStep(keyBytes, currentStep);
    }

    private String generateTotpForStep(byte[] keyBytes, long step) {
        try {
            byte[] data = ByteBuffer.allocate(8).putLong(step).array();
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(keyBytes, "RAW"));
            byte[] hash = mac.doFinal(data);

            int offset = hash[hash.length - 1] & 0x0F;
            long truncatedHash = ((hash[offset] & 0x7F) << 24)
                    | ((hash[offset + 1] & 0xFF) << 16)
                    | ((hash[offset + 2] & 0xFF) << 8)
                    | (hash[offset + 3] & 0xFF);

            long otp = truncatedHash % 1_000_000L;
            return String.format("%06d", otp);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error computing TOTP", e);
        }
    }

    private byte[] decodeBase32(String base32) {
        String clean = base32.toUpperCase().replaceAll("[^A-Z2-7]", "");
        byte[] bytes = new byte[clean.length() * 5 / 8];
        int buffer = 0;
        int bitsLeft = 0;
        int count = 0;

        for (char c : clean.toCharArray()) {
            int val = BASE32_CHARS.indexOf(c);
            if (val < 0) continue;
            buffer = (buffer << 5) | val;
            bitsLeft += 5;
            if (bitsLeft >= 8) {
                bytes[count++] = (byte) ((buffer >> (bitsLeft - 8)) & 0xFF);
                bitsLeft -= 8;
            }
        }
        return bytes;
    }
}
