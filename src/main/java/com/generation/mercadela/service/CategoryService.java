package com.generation.mercadela.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.generation.mercadela.model.Category;
import com.generation.mercadela.model.User;
import com.generation.mercadela.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final AuthenticationService authenticationService;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> getCategoriesByName(String name) {
        return categoryRepository.findAllByNameContainingIgnoreCase(name);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> updateCategory(Long categoryId, Category updatedCategory) {
        User currentUser = authenticationService.getCurrentUser();

        if (!currentUser.isAdmin()) {
            throw new AccessDeniedException("Only admins can update categories.");
        }

        return categoryRepository.findById(categoryId)
                .map(existingCategory -> {
                    existingCategory.setName(updatedCategory.getName());
                    return categoryRepository.save(existingCategory);
                });
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category not found.");
        }
        categoryRepository.deleteById(id);
    }
}
