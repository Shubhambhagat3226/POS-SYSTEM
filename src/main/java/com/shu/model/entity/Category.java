package com.shu.model.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a product category in a store.
 *
 * <p>
 * Categories are linked to a specific {@link Store} and can be associated with multiple products.
 * </p>
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    private Store store;
}
