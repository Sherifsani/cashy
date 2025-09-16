package com.cashy.cashy.category.service;

import com.cashy.cashy.auth.model.UserProfile;
import com.cashy.cashy.auth.service.UserService;
import com.cashy.cashy.category.dto.CategoryRequestDTO;
import com.cashy.cashy.category.dto.CategoryResponseDTO;
import com.cashy.cashy.category.exception.CategoryNotFoundException;
import com.cashy.cashy.category.mapper.CategoryMapper;
import com.cashy.cashy.category.model.Category;
import com.cashy.cashy.category.repository.CategoryRepository;
import com.cashy.cashy.transaction.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    // get all categories for user
    public List<CategoryResponseDTO> getCategories(UUID userId) {
        UserProfile targetUser = userService.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        List<Category> categories = targetUser.getCategories();
        return categories.stream().map(CategoryMapper::toDTO).collect(Collectors.toList());
    }

    // get a single category by id
    public Category getCategoryById(UUID categoryId) {
        return categoryRepository.findCategoryById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    // create a category
    public CategoryResponseDTO createCategory(UUID userId, CategoryRequestDTO requestDTO) {
        UserProfile targetUser = userService.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Category category = CategoryMapper.toEntity(requestDTO);
        category.setUserProfile(targetUser);

        categoryRepository.save(category);

        return CategoryMapper.toDTO(category);
    }

    // delete a category
    public void deleteCategory(UUID userId, UUID categoryId) {
        UserProfile targetUser = userService.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Category category = targetUser
                .getCategories()
                .stream()
                .filter(c -> c.getId().equals(categoryId))
                .findAny().orElseThrow(() -> new CategoryNotFoundException(categoryId));
        targetUser.getCategories().remove(category);
        categoryRepository.deleteById(categoryId);
    }

}
