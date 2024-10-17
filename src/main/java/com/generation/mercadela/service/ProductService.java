package com.generation.mercadela.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.generation.mercadela.model.Product;
import com.generation.mercadela.repository.ProductRepository;
import com.generation.mercadela.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Product updatedProduct) {
        validateProductOwnerOrAdmin(updatedProduct);

        return productRepository.findById(updatedProduct.getId())
                .map(existingProduct -> {
                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setPrice(updatedProduct.getPrice());
                    existingProduct.setDescription(updatedProduct.getDescription());
                    return productRepository.save(existingProduct);
                });
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found.");
        }
        validateProductOwnerOrAdmin(id);
        productRepository.deleteById(id);
    }

    private void validateProductOwnerOrAdmin(Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        System.out.println("Tipo de principal: " + principal.getClass().getName());

        if (principal instanceof UserDetailsImpl currentUser) {

            if (!currentUser.isAdmin() && !currentUser.getId().equals(product.getUser().getId())) {
                throw new AccessDeniedException("You can only update your own products.");
            }
        } else {
            throw new AccessDeniedException("Authentication required.");
        }
    }

    private void validateProductOwnerOrAdmin(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            validateProductOwnerOrAdmin(productOpt.get());
        } else {
            throw new IllegalArgumentException("Product not found.");
        }
    }
}
