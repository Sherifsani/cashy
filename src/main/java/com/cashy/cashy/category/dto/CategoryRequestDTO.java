package com.cashy.cashy.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class CategoryRequestDTO {

    @NotBlank(message = "enter the category name")
    private String categoryName;

}
