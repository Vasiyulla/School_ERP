package com.schoolerp.common.aspect;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.modules.system.service.TenantFeatureFlagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class FeatureEntitlementAspect {

    private final TenantFeatureFlagService featureFlagService;

    @Around("@annotation(requireFeature)")
    public Object enforceMethodFeature(ProceedingJoinPoint joinPoint, RequireFeature requireFeature) throws Throwable {
        String featureKey = requireFeature.value();
        if (!featureFlagService.isFeatureEnabled(featureKey)) {
            log.warn("Access denied to method {} due to unlicensed feature: {}", joinPoint.getSignature().toShortString(), featureKey);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Feature '" + featureKey + "' is not licensed or enabled for this school pod. Contact sales to upgrade.");
        }
        return joinPoint.proceed();
    }
}

