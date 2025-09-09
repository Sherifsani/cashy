package com.cashy.cashy.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class CategoryResponseDTO {
    private UUID categoryId;

    private String categoryName;
}
