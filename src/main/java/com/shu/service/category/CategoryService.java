package com.shu.service.category;

import com.shu.model.dto.CategoryDto;
import com.shu.service.category.impl.CategoryServiceImpl;

import java.util.List;

/**
 * CategoryService
 *
 * Defines the contract for managing product categories within stores.
 * Exposes CRUD operations that are implemented by {@link CategoryServiceImpl}.
 *
 * Responsibilities:
 * - Create, update, delete, and fetch categories.
 * - Enforce store-level ownership and role-based access.
 */
public interface CategoryService {

    /**
     * Create a new category in a store.
     * @param dto Category details including storeId and name.
     * @return Created category.
     */
    CategoryDto createCategory(CategoryDto dto);

    /**
     * Fetch all categories for a given store.
     * @param storeId ID of the store.
     * @return List of categories belonging to that store.
     */
    List<CategoryDto> getCategoriesByStore(Long storeId);

    /**
     * Update a category’s information.
     * @param id Category ID.
     * @param dto New category data.
     * @return Updated category.
     */
    CategoryDto updateCategory(Long id, CategoryDto dto);

    /**
     * Delete a category by ID.
     * @param id Category ID.
     */
    void deleteCategory(Long id);
}
