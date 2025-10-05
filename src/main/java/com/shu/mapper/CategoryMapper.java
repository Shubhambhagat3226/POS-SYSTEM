package com.shu.mapper;

import com.shu.model.dto.CategoryDto;
import com.shu.model.entity.Category;

public class CategoryMapper {

    public static CategoryDto toDTO(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .storeId(
                        category.getStore() != null ?
                                category.getStore().getId()
                                : null
                )
                .build();
    }

}
