package com.shu.mapper;

import com.shu.model.dto.StoreDto;
import com.shu.model.entity.Store;
import com.shu.model.entity.User;

public class StoreMapper {

    public static StoreDto toDTO(Store store) {
        return StoreDto.builder()
                .id(store.getId())
                .brand(store.getBrand())
                .description(store.getDescription())
                .storeAdmin(UserMapper.toDTO(store.getStoreAdmin()))
                .storeType(store.getStoreType())
                .contact(store.getContact())
                .createdAt(store.getCreatedAt())
                .updatedAt(store.getUpdatedAt())
                .status(store.getStatus())
                .build();
    }

    public static Store toEntity(StoreDto storeDto, User storeAdmin){
        Store store = new Store();
        store.setId(storeDto.getId());
        store.setBrand(storeDto.getBrand());
        store.setDescription(storeDto.getDescription());
        store.setStoreAdmin(storeAdmin);
        store.setStoreType(storeDto.getStoreType());
        store.setStatus(storeDto.getStatus());
        store.setContact(storeDto.getContact());
        store.setCreatedAt(storeDto.getCreatedAt());
        store.setUpdatedAt(storeDto.getUpdatedAt());

        return store;
    }
}
