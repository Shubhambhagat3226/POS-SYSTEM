package com.shu.controller;

import com.shu.constant.ApiPathConstant;
import com.shu.model.dto.ProductDto;
import com.shu.model.entity.User;
import com.shu.payload.response.APIResponse;
import com.shu.service.product.ProductService;
import com.shu.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPathConstant.PRODUCTS)
@Tag(name = "Product Management", description = "Endpoints for managing store products")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

    @PostMapping
    @Operation(
            summary = "Create a product",
            description = "Creates a new product under a store and category."
    )
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @ApiResponse(responseCode = "404", description = "Store or category not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized to create product")
    public ResponseEntity<ProductDto> create(
            @RequestBody ProductDto productDto
    ) {

        User user = userService.getCurrentUser();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        productService
                                .createProduct(productDto, user)
                );
    }

    @GetMapping("/store/{storeId}")
    @Operation(
            summary = "Get products by store",
            description = "Fetches all products under a given store."
    )
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Store not found")
    public ResponseEntity<List<ProductDto>> getByStoreId(
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(
                        productService
                                .getProductsByStoreId(storeId)
                );
    }

    @GetMapping("/store/{storeId}/search")
    @Operation(
            summary = "Search products by keyword",
            description = "Searches for products within a store matching a keyword."
    )
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Store not found")
    public ResponseEntity<List<ProductDto>> searchByKeyword(
            @PathVariable Long storeId,
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(productService
                        .searchByKeyword(storeId, keyword));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a product",
            description = "Updates product details. Only authorized users can update."
    )
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized to update product")
    public ResponseEntity<ProductDto> update(
            @PathVariable Long id,
            @RequestBody ProductDto productDto
    ) {

        User user = userService.getCurrentUser();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.updateProduct(id, productDto, user));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a product",
            description = "Deletes a product by ID. Only authorized users can delete."
    )
    @ApiResponse(responseCode = "200", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized to delete product")
    public ResponseEntity<APIResponse> delete(
            @PathVariable Long id
    ) {

        User user = userService.getCurrentUser();
        productService.deleteProduct(id, user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIResponse.builder()
                        .message("Product deleted successfully")
                        .build());
    }
}
