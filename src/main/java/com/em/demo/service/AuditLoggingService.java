package com.em.demo.service;

import com.em.demo.model.AuditLog;
import com.em.demo.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLoggingService {

    private final AuditLogRepository auditLogRepository;

    @Async
    public void logAction(String action, String entityName, String entityId, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setEntityName(entityName);
        auditLog.setEntityId(entityId);
        auditLog.setDetails(details);
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setExcuiter("temp_user");

        auditLogRepository.save(auditLog);
    }
}
