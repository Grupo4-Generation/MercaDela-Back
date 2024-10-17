package com.generation.mercadela.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.generation.mercadela.model.Category;
import com.generation.mercadela.repository.CategoryRepository;
import com.generation.mercadela.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

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
        validateAdminPermission();
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    public Optional<Category> updateCategory(Category updatedCategory) {
        validateAdminPermission();

        return categoryRepository.findById(updatedCategory.getId())
                .map(existingCategory -> {
                    existingCategory.setName(updatedCategory.getName());
                    return categoryRepository.save(existingCategory);
                });
    }

    public ResponseEntity<?> deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        validateAdminPermission(); // Verificação de permissão

        categoryRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private void validateAdminPermission() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        System.out.println("Tipo de principal: " + principal.getClass().getName());

        if (principal instanceof UserDetailsImpl currentUser) {
            if (!currentUser.isAdmin()) {
                throw new AccessDeniedException("Only admins can perform this action.");
            }
        } else {
            throw new AccessDeniedException("Authentication required.");
        }
    }
}
