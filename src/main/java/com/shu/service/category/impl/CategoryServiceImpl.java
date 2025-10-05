package com.shu.service.category.impl;

import com.shu.domain.UserRole;
import com.shu.exceptions.CategoryException;
import com.shu.exceptions.StoreException;
import com.shu.exceptions.UserException;
import com.shu.mapper.CategoryMapper;
import com.shu.model.dto.CategoryDto;
import com.shu.model.entity.Category;
import com.shu.model.entity.Store;
import com.shu.model.entity.User;
import com.shu.repository.CategoryRepository;
import com.shu.repository.StoreRepository;
import com.shu.service.category.CategoryService;
import com.shu.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CategoryServiceImpl
 *
 * Implements {@link CategoryService} with security, validation, and persistence logic.
 *
 * Security:
 * - Only users with ROLE_STORE_ADMIN (owning store) or ROLE_STORE_MANAGER can modify categories.
 *
 * Dependencies:
 * - CategoryRepository for persistence.
 * - UserService for identifying the logged-in user.
 * - StoreRepository for validating store ownership.
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final StoreRepository storeRepository;

    /**
     * Validates that the user has authority to manage categories for a store.
     * @param user  The current authenticated user.
     * @param store The store being managed.
     * @throws UserException if the user lacks permission.
     */
    private void checkAuthority(User user, Store store) {
        boolean isAdmin = user.getRole().equals(UserRole.ROLE_STORE_ADMIN);
        boolean isManager = user.getRole().equals(UserRole.ROLE_STORE_MANAGER);
        boolean isSameStore = store.getStoreAdmin().getId().equals(user.getId());

        if (!(isAdmin && isSameStore) && !isManager) {
            throw new UserException(
                    "You don't have permission to manage this category",
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        User user = userService.getCurrentUser();

        Store store = storeRepository
                .findById(dto.getStoreId())
                .orElseThrow(
                        () -> new StoreException("Store not found", HttpStatus.NOT_FOUND)
                );

        checkAuthority(user, store);

        Category category = Category.builder()
                .store(store)
                .name(dto.getName())
                .build();

        return CategoryMapper
                .toDTO(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> getCategoriesByStore(Long storeId) {
        List<Category> categories = categoryRepository
                .findByStoreId(storeId);
        return categories.stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(
                        () -> new CategoryException(
                                "Category not exist",
                                HttpStatus.NOT_FOUND
                        )
                );

        User user = userService.getCurrentUser();
        checkAuthority(user, category.getStore());

        if (dto.getName() != null && !dto.getName().isBlank()) {
            category.setName(dto.getName());
        }

        return CategoryMapper.toDTO(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(
                        () -> new CategoryException(
                                "Category not exist",
                                HttpStatus.NOT_FOUND
                        )
                );

        User user = userService.getCurrentUser();
        checkAuthority(user, category.getStore());
        categoryRepository.delete(category);
    }
}
