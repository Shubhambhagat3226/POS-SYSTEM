package com.shu.service.store;

import com.shu.model.dto.StoreDto;
import com.shu.model.entity.Store;
import com.shu.model.entity.User;

import java.util.List;

public interface StoreService {

    StoreDto createStore(StoreDto storeDto, User user);
    StoreDto getStoreById(Long id);
    List<StoreDto> getAllStores();
    Store getStoreByAdmin();
    StoreDto updateStore(Long id, StoreDto storeDto);
    StoreDto deleteStore(Long id);
    StoreDto getStoreByEmployee();
}
