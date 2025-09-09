package com.cashy.cashy.category.controller;

import com.cashy.cashy.category.dto.CategoryRequestDTO;
import com.cashy.cashy.category.dto.CategoryResponseDTO;
import com.cashy.cashy.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/{userId}/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @PathVariable UUID userId,
            @RequestBody CategoryRequestDTO categoryRequestDTO
            ){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(userId, categoryRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.getCategories());
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable UUID categoryId,
            @PathVariable UUID userId
    ){
        categoryService.deleteCategory(userId, categoryId);
        return ResponseEntity.ok("Category deleted successfully");
    }
}
