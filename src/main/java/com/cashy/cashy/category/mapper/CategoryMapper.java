package com.cashy.cashy.category.mapper;

import com.cashy.cashy.category.dto.CategoryRequestDTO;
import com.cashy.cashy.category.dto.CategoryResponseDTO;
import com.cashy.cashy.category.model.Category;

public class CategoryMapper {
    public static Category toEntity(CategoryRequestDTO categoryRequestDTO) {
        return Category.builder()
                .categoryName(categoryRequestDTO.getCategoryName())
                .build();
    }

    public static CategoryResponseDTO toDTO(Category category) {
        return CategoryResponseDTO.builder()
                .categoryName(category.getCategoryName())
                .categoryId(category.getId())
                .build();
    }
}
