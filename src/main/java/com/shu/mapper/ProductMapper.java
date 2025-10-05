package com.shu.mapper;

import com.shu.model.dto.ProductDto;
import com.shu.model.entity.Category;
import com.shu.model.entity.Product;
import com.shu.model.entity.Store;

public class ProductMapper {

    public static ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .description(product.getDescription())
                .sku(product.getSku())
                .mrp(product.getMrp())
                .sellingPrice(product.getSellingPrice())
                .image(product.getImage())
                .storeId(
                        product.getStore()!=null
                                ?product.getStore().getId()
                                :null
                )
                .category(
                        CategoryMapper.toDTO(
                                product.getCategory()
                        )
                )
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public static Product toEntity(ProductDto productDto,
                                   Store store,
                                   Category category) {
        return Product.builder()
                .name(productDto.getName())
                .brand(productDto.getBrand())
                .description(productDto.getDescription())
                .mrp(productDto.getMrp())
                .sellingPrice(productDto.getSellingPrice())
                .sku(productDto.getSku())
                .store(store)
                .category(category)
                .image(productDto.getImage())
                .build();
    }
}
