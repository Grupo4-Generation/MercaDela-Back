package com.generation.mercadela.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;

import com.generation.mercadela.model.Category;
import com.generation.mercadela.model.User;
import com.generation.mercadela.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final UserService userService;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> getCategoriesByName(String name) {
        return categoryRepository.findAllByNameContainingIgnoreCase(name);
    }

    public ResponseEntity<?> createCategory(Category category) {
        User loggedUser = userService.getLoggedInUser();
        if (!loggedUser.isAdmin()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    public Optional<Category> updateCategory(Long categoryId, Category updatedCategory) {

        if (!userService.getLoggedInUser().isAdmin()) {
            throw new AccessDeniedException("Only admins can update categories.");
        }

        return categoryRepository.findById(categoryId)
                .map(existingCategory -> {
                    existingCategory.setName(updatedCategory.getName());
                    return categoryRepository.save(existingCategory);
                });
    }

    public ResponseEntity<?>  deleteCategory(Long id) {
        User loggedUser = userService.getLoggedInUser();
        if (!categoryRepository.existsById(id)) {
            if (!loggedUser.isAdmin()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } else {
            categoryRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }
}
