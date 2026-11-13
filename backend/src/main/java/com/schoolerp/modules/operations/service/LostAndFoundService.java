package com.schoolerp.modules.operations.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.operations.dto.ClaimLostFoundRequest;
import com.schoolerp.modules.operations.dto.LogLostFoundRequest;
import com.schoolerp.modules.operations.entity.LostAndFoundItem;
import com.schoolerp.modules.operations.repository.LostAndFoundItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LostAndFoundService {

    private final LostAndFoundItemRepository lostAndFoundRepository;

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant-default";
    }

    @Transactional
    public LostAndFoundItem logFoundItem(LogLostFoundRequest request, String reporterStaffName) {
        String tenantId = resolveTenant();

        long count = lostAndFoundRepository.countByTenantId(tenantId);
        String trackingCode = String.format("LF-%d-%04d", LocalDate.now().getYear(), count + 1);

        LostAndFoundItem item = LostAndFoundItem.builder()
                .tenantId(tenantId)
                .trackingCode(trackingCode)
                .itemCategory(request.getItemCategory().toUpperCase())
                .itemDescription(request.getItemDescription())
                .foundLocation(request.getFoundLocation())
                .foundDate(request.getFoundDate())
                .reportedByStaffName(reporterStaffName != null ? reporterStaffName : "Campus Staff")
                .photoUrl(request.getPhotoUrl())
                .storageLockerRef(request.getStorageLockerRef() != null ? request.getStorageLockerRef() : "Security Office Vault A-1")
                .status("FOUND_IN_CUSTODY")
                .build();

        log.info("New lost & found item logged: {} ({}) found at {}", trackingCode, item.getItemCategory(), item.getFoundLocation());
        return lostAndFoundRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<LostAndFoundItem> getItems(String status, String category) {
        String tenantId = resolveTenant();

        if (status != null && !status.isBlank() && !"ALL".equalsIgnoreCase(status)) {
            return lostAndFoundRepository.findAllByTenantIdAndStatusOrderByCreatedAtDesc(tenantId, status.toUpperCase());
        }
        if (category != null && !category.isBlank() && !"ALL".equalsIgnoreCase(category)) {
            return lostAndFoundRepository.findAllByTenantIdAndItemCategoryOrderByCreatedAtDesc(tenantId, category.toUpperCase());
        }
        return lostAndFoundRepository.findAllByTenantIdOrderByCreatedAtDesc(tenantId);
    }

    @Transactional
    public LostAndFoundItem processClaim(UUID itemId, ClaimLostFoundRequest request) {
        String tenantId = resolveTenant();

        LostAndFoundItem item = lostAndFoundRepository.findByIdAndTenantId(itemId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Lost & found item not found with ID: " + itemId));

        if ("CLAIMED_RETURNED".equalsIgnoreCase(item.getStatus())) {
            throw new IllegalStateException("Item has already been claimed and handed over.");
        }

        item.setStatus("CLAIMED_RETURNED");
        item.setClaimedByStudentId(request.getClaimedByStudentId());
        item.setClaimedByName(request.getClaimedByName());
        item.setClaimVerificationNote(request.getClaimVerificationNote());
        item.setReturnedAt(LocalDateTime.now());

        log.info("Lost & found item {} claimed by {} with note: {}", item.getTrackingCode(), request.getClaimedByName(), request.getClaimVerificationNote());
        return lostAndFoundRepository.save(item);
    }
}

