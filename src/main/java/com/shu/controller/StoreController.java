package com.shu.controller;

import com.shu.constant.ApiPathConstant;
import com.shu.constant.OpenApiConstants;
import com.shu.domain.StoreStatus;
import com.shu.model.dto.StoreDto;
import com.shu.model.entity.Store;
import com.shu.model.entity.User;
import com.shu.payload.response.APIResponse;
import com.shu.service.store.StoreService;
import com.shu.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * StoreController
 *
 * REST controller for managing {@link Store} entities.
 * Provides endpoints to create, fetch, update, moderate, and delete stores.
 * Includes role-based access for Admins and Employees.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPathConstant.STORE)
@Tag(name = "Stores", description = "Endpoints for managing store operations")
@SecurityRequirement(name = OpenApiConstants.SECURITY_SCHEME_NAME)
public class StoreController {

    private final StoreService storeService;
    private final UserService userService;

    /**
     * Create a new store for the authenticated user.
     *
     * @param storeDto Store details
     * @return Created store as DTO
     */
    @PostMapping
    @Operation(summary = "Create store", description = "Creates a new store linked to the authenticated user.")
    @ApiResponse(responseCode = "201", description = "Store created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT")
    public ResponseEntity<StoreDto> createStore(
            @RequestBody StoreDto storeDto) {
        User user = userService.getCurrentUser();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(storeService.createStore(storeDto, user));
    }

    /**
     * Retrieve a store by its ID.
     *
     * @param id Store ID
     * @return Store details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get store by ID", description = "Fetches store details by ID.")
    @ApiResponse(responseCode = "200", description = "Store found")
    @ApiResponse(responseCode = "404", description = "Store not found")
    public ResponseEntity<StoreDto> getStoreById(
            @PathVariable("id") Long id) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.getStoreById(id));
    }

    /**
     * Retrieve all available stores.
     *
     * @return List of stores
     */
    @GetMapping
    @Operation(summary = "Get all stores", description = "Fetches all available stores.")
    @ApiResponse(responseCode = "200", description = "Stores retrieved successfully")
    public ResponseEntity<List<StoreDto>> getAllStore() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.getAllStores());
    }

    /**
     * Get the store managed by the currently authenticated admin.
     *
     * @return Store entity
     */
    @GetMapping("/admin")
    @Operation(summary = "Get store by admin", description = "Fetches the store owned by the authenticated admin.")
    @ApiResponse(responseCode = "200", description = "Store retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Store not found for this admin")
    @ApiResponse(responseCode = "401", description = "Unauthorized - not an admin or invalid JWT")
    public ResponseEntity<Store> getStoreByAdmin() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.getStoreByAdmin());
    }

    /**
     * Get the store associated with the currently authenticated employee.
     *
     * @return Store details
     */
    @GetMapping("/employee")
    @Operation(summary = "Get store by employee", description = "Fetches the store assigned to the authenticated employee.")
    @ApiResponse(responseCode = "200", description = "Store retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized - not an employee or invalid JWT")
    public ResponseEntity<StoreDto> getStoreByEmployee() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.getStoreByEmployee());
    }

    /**
     * Update store details by ID.
     *
     * @param id       Store ID
     * @param storeDto Updated store data
     * @return Updated store
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update store", description = "Updates the details of a store by its ID.")
    @ApiResponse(responseCode = "200", description = "Store updated successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized - user does not own this store")
    @ApiResponse(responseCode = "404", description = "Store not found")
    public ResponseEntity<StoreDto> updateStore(
            @PathVariable Long id,
            @RequestBody StoreDto storeDto
    ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.updateStore(id, storeDto));
    }

    /**
     * Moderate a store by updating its status (e.g., ACTIVE, BLOCKED).
     *
     * @param id     Store ID
     * @param status New status
     * @return Updated store
     */
    @PatchMapping("/{id}/moderate")
    @Operation(summary = "Moderate store", description = "Updates the status of a store (e.g., APPROVED, REJECTED).")
    @ApiResponse(responseCode = "200", description = "Store status updated successfully")
    @ApiResponse(responseCode = "404", description = "Store not found")
    public ResponseEntity<StoreDto> moderateStore(
            @PathVariable Long id,
            @RequestParam StoreStatus status
            ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.moderateStore(id, status));
    }

    /**
     * Delete a store by ID. Only the store admin can perform this operation.
     *
     * @param id Store ID
     * @return Success message
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete store", description = "Deletes a store by ID. Only the store admin can delete their store.")
    @ApiResponse(responseCode = "200", description = "Store deleted successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized - user does not own this store")
    @ApiResponse(responseCode = "404", description = "Store not found")
    public ResponseEntity<APIResponse> deleteStore(
            @PathVariable Long id
    ) {

        storeService.deleteStore(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIResponse.builder()
                        .message("Store deleted successfully")
                        .build());
    }
}
