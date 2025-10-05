package com.shu.service.product;

import com.shu.model.dto.ProductDto;
import com.shu.model.entity.User;

import java.util.List;

/**
 * Service interface for managing products in the POS system.
 *
 * <p>This interface defines operations related to product creation, update,
 * deletion, retrieval, and search. Implementations are responsible for handling
 * validation, mapping, and business rules associated with store and category relationships.</p>
 */
public interface ProductService {

    /**
     * Creates a new product under a specific store and category.
     *
     * @param productDto DTO containing product details (name, SKU, price, etc.)
     * @param user the currently authenticated user performing the operation
     * @return the created {@link ProductDto} containing generated product details
     * @throws com.shu.exceptions.StoreException if the store does not exist
     * @throws com.shu.exceptions.CategoryException if the category does not exist
     */
    ProductDto createProduct(ProductDto productDto, User user);

    /**
     * Updates an existing product.
     *
     * @param id ID of the product to update
     * @param productDto DTO containing updated product details
     * @param user the currently authenticated user performing the operation
     * @return the updated {@link ProductDto}
     * @throws com.shu.exceptions.ProductException if the product does not exist
     */
    ProductDto updateProduct(Long id, ProductDto productDto, User user);

    /**
     * Deletes a product by ID.
     *
     * @param id ID of the product to delete
     * @param user the currently authenticated user performing the operation
     * @throws com.shu.exceptions.ProductException if the product does not exist
     */
    void deleteProduct(Long id, User user);

    /**
     * Retrieves all products belonging to a specific store.
     *
     * @param storeId ID of the store
     * @return a list of {@link ProductDto} belonging to the store
     */
    List<ProductDto> getProductsByStoreId(Long storeId);

    /**
     * Searches for products within a store by keyword (name, brand, SKU).
     *
     * @param storeId ID of the store
     * @param keyword the search keyword
     * @return a list of {@link ProductDto} matching the keyword
     */
    List<ProductDto> searchByKeyword(Long storeId, String keyword);

}
