package com.shu.service.store;

import com.shu.domain.StoreStatus;
import com.shu.model.dto.StoreDto;
import com.shu.model.entity.Store;
import com.shu.model.entity.User;

import java.util.List;

/**
 * Service interface for managing {@link Store}.
 * <p>
 * Defines business logic operations such as creating, updating,
 * moderating, deleting, and retrieving store details.
 */
public interface StoreService {

    /**
     * Creates a new store for the given user.
     *
     * @param storeDto Store details
     * @param user     Store admin (creator)
     * @return Created store DTO
     */
    StoreDto createStore(StoreDto storeDto, User user);

    /**
     * Retrieves store details by ID.
     *
     * @param id Store ID
     * @return Store DTO
     */
    StoreDto getStoreById(Long id);

    /**
     * Retrieves all stores in the system.
     *
     * @return List of store DTOs
     */
    List<StoreDto> getAllStores();

    /**
     * Retrieves the store owned by the current admin.
     *
     * @return Store entity
     */
    Store getStoreByAdmin();

    /**
     * Updates store details for a given ID.
     *
     * @param id       Store ID
     * @param storeDto Updated store details
     * @return Updated store DTO
     */
    StoreDto updateStore(Long id, StoreDto storeDto);

    /**
     * Deletes a store by ID.
     *
     * @param id Store ID
     */
    void deleteStore(Long id);

    /**
     * Retrieves the store linked to the current employee.
     *
     * @return Store DTO
     */
    StoreDto getStoreByEmployee();

    /**
     * Updates the status of a store (moderation).
     *
     * @param id     Store ID
     * @param status New store status
     * @return Updated store DTO
     */
    StoreDto moderateStore(Long id, StoreStatus status);

}
