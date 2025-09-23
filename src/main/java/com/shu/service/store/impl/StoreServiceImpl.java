package com.shu.service.store.impl;

import com.shu.exceptions.StoreException;
import com.shu.mapper.StoreMapper;
import com.shu.model.dto.StoreDto;
import com.shu.model.entity.Store;
import com.shu.model.entity.User;
import com.shu.repository.StoreRepository;
import com.shu.service.store.StoreService;
import com.shu.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserService userService;

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
        return List.of();
    }

    @Override
    public Store getStoreByAdmin() {
        return null;
    }

    @Override
    public StoreDto updateStore(Long id, StoreDto storeDto) {
        return null;
    }

    @Override
    public StoreDto deleteStore(Long id) {
        return null;
    }

    @Override
    public StoreDto getStoreByEmployee() {
        return null;
    }
}
