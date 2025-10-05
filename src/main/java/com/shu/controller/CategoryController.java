package com.shu.controller;

import com.shu.constant.ApiPathConstant;
import com.shu.model.dto.CategoryDto;
import com.shu.payload.response.APIResponse;
import com.shu.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;


/**
 * CategoryController
 *
 * Provides REST APIs for managing product categories within a store.
 * Only users with ROLE_STORE_ADMIN (owning the store) or ROLE_STORE_MANAGER
 * are allowed to create, update, or delete categories.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPathConstant.CATEGORY)
@Tag(name = "Category Management", description = "APIs for creating, updating, retrieving and deleting store categories.")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Create a category", description = "Creates a new category for a store. Only store admins or managers can create.")
    @ApiResponse(responseCode = "201", description = "Category created successfully")
    @ApiResponse(responseCode = "404", description = "Store not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized to create category")
    public ResponseEntity<CategoryDto> create(
            @RequestBody CategoryDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createCategory(dto));
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "Get categories by store", description = "Fetches all categories for a given store.")
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Store not found")
    public ResponseEntity<List<CategoryDto>> getCategoriesByStoreId(
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(
                categoryService.getCategoriesByStore(storeId)
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category", description = "Updates an existing category. Only store admins or managers can update.")
    @ApiResponse(responseCode = "200", description = "Category updated successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized to update category")
    public ResponseEntity<CategoryDto> update(
            @PathVariable Long id,
            @RequestBody CategoryDto dto
    ) {
        return ResponseEntity.ok(
                categoryService.updateCategory(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category", description = "Deletes a category by ID. Only store admins or managers can delete.")
    @ApiResponse(responseCode = "200", description = "Category deleted successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized to delete category")
    public ResponseEntity<APIResponse> delete(
            @PathVariable Long id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(
                APIResponse.builder()
                        .message("Category deleted successfully")
                        .build()
        );
    }
}
