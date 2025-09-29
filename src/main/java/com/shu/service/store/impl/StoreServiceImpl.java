package com.shu.service.store.impl;

import com.shu.domain.StoreStatus;
import com.shu.exceptions.StoreException;
import com.shu.exceptions.UserException;
import com.shu.mapper.StoreMapper;
import com.shu.model.dto.StoreDto;
import com.shu.model.entity.Store;
import com.shu.model.entity.StoreContact;
import com.shu.model.entity.User;
import com.shu.repository.StoreRepository;
import com.shu.service.store.StoreService;
import com.shu.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of {@link StoreService}.
 * <p>
 * Provides business logic for managing stores including create, update,
 * delete, fetch by role, and store moderation.
 */
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserService userService;

    /**
     * Applies updates from a {@link StoreDto} to an existing {@link Store}.
     *
     * @param existing Existing store entity
     * @param dto      Updated store details
     */
    private void applyUpdates(Store existing, StoreDto dto) {
        if (dto.getBrand() != null && !dto.getBrand().isBlank()) {
            existing.setBrand(dto.getBrand());
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            existing.setDescription(dto.getDescription());
        }
        if (dto.getStoreType() != null && !dto.getStoreType().isBlank()) {
            existing.setStoreType(dto.getStoreType());
        }
        if (dto.getContact() != null) {
            existing.setContact(getStoreContact(dto, existing));
        }
    }

    /**
     * Updates contact details for a store if provided in the DTO.
     *
     * @param storeDto Store DTO with new contact info
     * @param existing Existing store
     * @return Updated contact object
     */
    private static StoreContact getStoreContact(StoreDto storeDto, Store existing) {
        StoreContact oldContact = storeDto.getContact();
        StoreContact newContact = existing.getContact();

        if (oldContact != null) {
            if (oldContact.getAddress() != null && !oldContact.getAddress().isBlank()) {
                newContact.setAddress(oldContact.getAddress());
            }
            if (oldContact.getEmail() != null && !oldContact.getEmail().isBlank()) {
                newContact.setEmail(oldContact.getEmail());
            }
            if (oldContact.getPhone() != null && !oldContact.getPhone().isBlank()) {
                newContact.setPhone(oldContact.getPhone());
            }
        }
        return newContact;
    }

    @Override
    public StoreDto createStore(StoreDto storeDto, User user) {

        Store store = StoreMapper.toEntity(storeDto, user);

        return StoreMapper.toDTO(storeRepository.save(store));
    }

    @Override
    public StoreDto getStoreById(Long id) {

        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new StoreException("Store not found", HttpStatus.NOT_FOUND));
        return StoreMapper.toDTO(store);
    }

    @Override
    public List<StoreDto> getAllStores() {
        return storeRepository.findAll()
                .stream()
                .map(StoreMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Store getStoreByAdmin() {
        User admin = userService.getCurrentUser();
        Store store = storeRepository.findByStoreAdminId(admin.getId());
        if (store == null) throw new StoreException("Store not found", HttpStatus.NOT_FOUND);
        return store;
    }

    @Override
    public StoreDto updateStore(Long id, StoreDto storeDto) {
        User currentUser = userService.getCurrentUser();
        Store existing = storeRepository.findByStoreAdminId(currentUser.getId());

        if (existing == null) {
            throw new StoreException("Store not Found", HttpStatus.NOT_FOUND);
        }

        if (!existing.getId().equals(id)) {
            throw new StoreException("You cannot update this store", HttpStatus.UNAUTHORIZED);
        }

        applyUpdates(existing, storeDto);

        return StoreMapper.toDTO(storeRepository.save(existing));
    }

    @Override
    public void deleteStore(Long id) {

        Store store = getStoreByAdmin();
        if (!Objects.equals(store.getId(), id)) {
            throw new StoreException("You cannot delete this store", HttpStatus.UNAUTHORIZED);
        }

        storeRepository.delete(store);
    }

    @Override
    public StoreDto getStoreByEmployee() {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getStore() == null) {
            throw new UserException("You don't have permission to access this store.", HttpStatus.UNAUTHORIZED);
        }
        return StoreMapper.toDTO(currentUser.getStore());
    }

    @Override
    public StoreDto moderateStore(Long id, StoreStatus status) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() ->
                        new StoreException("Store not found",
                                HttpStatus.NOT_FOUND));

        store.setStatus(status);
        Store updatedStore = storeRepository.save(store);
        return StoreMapper.toDTO(updatedStore);
    }
}
