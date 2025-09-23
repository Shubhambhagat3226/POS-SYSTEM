package com.shu.model.dto;

import com.shu.domain.StoreStatus;
import com.shu.model.entity.StoreContact;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StoreDto {

    private Long id;

    private String brand;

    private UserDto storeAdmin;

    private String description;

    private String storeType;

    private StoreStatus status;

    private StoreContact contact;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
