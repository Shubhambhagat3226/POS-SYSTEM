package com.shu.model.entity;

import com.shu.domain.StoreStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 *
 * STORE ENTITY:
 * Represents a store in the POS system. Contains information such as brand, type,
 * description, contact details, administrator, and lifecycle status.
 *
 * Fields:
 * - id          : Unique identifier for the store (Primary Key).
 * - brand       : Store brand name (required).
 * - storeAdmin  : The administrator (User) responsible for managing the store.
 * - description : Optional description of the store.
 * - storeType   : Type/category of the store (e.g., retail, wholesale).
 * - status      : Current status of the store (ACTIVE, PENDING, BLOCKED).
 * - contact     : Embedded StoreContact with address, phone, and email.
 * - createdAt   : Timestamp when the store was created.
 * - updatedAt   : Timestamp of the last modification.
 *
 * Annotations:
 * - @Entity            : Marks this as a JPA entity.
 * - @Id, @GeneratedValue: Primary key with auto-generation.
 * - @OneToOne          : Relation to the User entity for store administrator.
 * - @Enumerated        : Persists StoreStatus enum as a string.
 * - @PrePersist        : Lifecycle callback to set createdAt and default status.
 * - @PreUpdate         : Lifecycle callback to update the updatedAt field.
 * - Lombok annotations : @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @EqualsAndHashCode
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @OneToOne
    private User storeAdmin;

    private String description;

    private String storeType;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @Embedded
    private StoreContact contact = new StoreContact();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Lifecycle callback executed before persisting a new store.
     * Sets createdAt timestamp and initializes status as PENDING.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = StoreStatus.PENDING;
    }

    /**
     * Lifecycle callback executed before updating an existing store.
     * Updates the updatedAt timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
