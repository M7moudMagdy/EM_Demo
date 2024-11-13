package com.em.demo.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private String entityName;
    private String entityId;
    private String details;
    private LocalDateTime timestamp;

    private String Excuiter;  // Assuming you have a User entity representing the user who performed the action
}
