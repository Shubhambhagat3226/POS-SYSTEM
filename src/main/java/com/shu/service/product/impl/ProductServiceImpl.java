package com.shu.service.product.impl;

import com.shu.exceptions.CategoryException;
import com.shu.exceptions.ProductException;
import com.shu.exceptions.StoreException;
import com.shu.mapper.ProductMapper;
import com.shu.model.dto.ProductDto;
import com.shu.model.entity.Category;
import com.shu.model.entity.Product;
import com.shu.model.entity.Store;
import com.shu.model.entity.User;
import com.shu.repository.CategoryRepository;
import com.shu.repository.ProductRepository;
import com.shu.repository.StoreRepository;
import com.shu.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link ProductService} for managing product data.
 *
 * <p>Handles CRUD operations for products, including validation of related
 * entities like store and category. Utilizes {@link ProductMapper} for DTO mapping.</p>
 */

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Applies updates from the provided DTO to an existing {@link Product} entity.
     *
     * @param product the target entity to update
     * @param productDto the DTO containing updated values
     */
    private void applyUpdate(Product product, ProductDto productDto) {

        if (productDto.getCategoryId() != null) {
            Category category = categoryRepository
                    .findById(productDto.getCategoryId())
                    .orElseThrow(
                            () -> new CategoryException("Category not found" , HttpStatus.NOT_FOUND)
                    );
            product.setCategory(category);
        }

        if (productDto.getName() != null && !productDto.getName().isBlank()) {
            product.setName(productDto.getName());
        }
        if (productDto.getBrand() != null && !productDto.getBrand().isBlank()) {
            product.setBrand(productDto.getBrand());
        }
        if (productDto.getDescription() != null && !productDto.getDescription().isBlank()) {
            product.setDescription(productDto.getDescription());
        }
        if (productDto.getSku() != null && !productDto.getSku().isBlank()) {
            product.setSku(productDto.getSku());
        }
        if (productDto.getImage() != null && !productDto.getImage().isBlank()) {
            product.setImage(productDto.getImage());
        }
        if (productDto.getSellingPrice() != null) {
            product.setSellingPrice(productDto.getSellingPrice());
        }
        if (productDto.getMrp() != null) {
            product.setMrp(productDto.getMrp());
        }

    }

    @Override
    public ProductDto createProduct(ProductDto productDto, User user) {
        Store store = storeRepository
                .findById(productDto.getStoreId())
                .orElseThrow(() -> new StoreException("Store not found", HttpStatus.NOT_FOUND));

        Category category = categoryRepository
                .findById(productDto.getCategoryId())
                .orElseThrow(
                        () -> new CategoryException("Category not found" , HttpStatus.NOT_FOUND)
                );

        Product product = ProductMapper.toEntity(productDto, store, category);
        Product saveProduct = productRepository.save(product);
        return ProductMapper.toDto(saveProduct);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto, User user) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ProductException("Product not found", HttpStatus.NOT_FOUND));

        applyUpdate(product, productDto);

        Product saveProduct = productRepository.save(product);
        return ProductMapper.toDto(saveProduct);
    }

    @Override
    public void deleteProduct(Long id, User user) {

        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ProductException("Product not found", HttpStatus.NOT_FOUND));

        productRepository.delete(product);
    }

    @Override
    public List<ProductDto> getProductsByStoreId(Long storeId) {
        List<Product> products = productRepository
                .findByStoreId(storeId);
        return products.stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> searchByKeyword(Long storeId, String keyword) {
        List<Product> products = productRepository
                .searchByKeyword(storeId, keyword);
        return products.stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }
}
