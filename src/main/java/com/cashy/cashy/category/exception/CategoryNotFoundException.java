package com.cashy.cashy.category.exception;

import com.cashy.cashy.exception.ResourceNotFoundException;

import java.util.UUID;

public class CategoryNotFoundException extends ResourceNotFoundException {
    public CategoryNotFoundException(UUID categoryId) {
        super("Category", categoryId);
    }
}
