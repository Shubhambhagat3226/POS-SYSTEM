package com.shu.model.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import lombok.Data;

/**
 * STORE CONTACT:
 * Represents contact details of a store. Embedded inside the Store entity.
 *
 * Fields:
 * - address : Physical address of the store.
 * - phone   : Contact phone number of the store.
 * - email   : Contact email, validated to ensure proper format.
 *
 * Annotations:
 * - @Embeddable       : Marks this class as an embeddable JPA component.
 * - @Email            : Ensures the email field follows valid email format.
 * - Lombok @Data      : Generates getters, setters, equals, hashCode, and toString.
 */
@Data
@Embeddable
public class StoreContact {
    private String address;
    private String phone;

    @Email(message = "Invalid email format")
    private String email;
}
