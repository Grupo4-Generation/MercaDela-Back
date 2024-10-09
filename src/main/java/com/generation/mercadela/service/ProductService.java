package com.generation.mercadela.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.generation.mercadela.model.Product;
import com.generation.mercadela.model.User;
import com.generation.mercadela.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private AuthenticationService authenticationService; 
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

    public Optional<Product> updateProduct(Long productId, Product updatedProduct) {
        User currentUser = authenticationService.getCurrentUser(); // 

        // Verifica se o ID do usuário associado ao produto corresponde ao ID do usuário logado ou se o usuário é um administrador
        if (!currentUser.isAdmin() && !currentUser.getId().equals(updatedProduct.getUser().getId())) {
            throw new AccessDeniedException("You can only update your own products.");
        }

        return productRepository.findById(productId)
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
        productRepository.deleteById(id);
    }
}
