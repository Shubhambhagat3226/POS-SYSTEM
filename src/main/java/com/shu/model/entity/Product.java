package com.shu.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a product available in the store.
 *
 * <p>
 * A product belongs to a {@link Category} and a {@link Store}. Includes details
 * like SKU, pricing, brand, and timestamps for creation and updates.
 * </p>
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String sku;

    private String description;

    private Double mrp;

    private Double sellingPrice;
    private String brand;
    private String image;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Store store;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Lifecycle callback executed before persisting a new product.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Lifecycle callback executed before updating an existing product.
     * Updates the updatedAt timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
