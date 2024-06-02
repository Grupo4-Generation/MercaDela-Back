package com.generation.mercadela.controller;

import com.generation.mercadela.model.Product;
import com.generation.mercadela.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @GetMapping
    public ResponseEntity<List<Product>> getAll(){
        return ResponseEntity.ok(productRepository.findAll());

    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id){
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<List<Product>> getByNome(@PathVariable String name){
        return ResponseEntity.ok(productRepository.findByNameContainingIgnoreCase(name));
    }

    @PostMapping
    public ResponseEntity<Product> post(@Valid @RequestBody Product product){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productRepository.save(product));
    }

    @PutMapping
    public ResponseEntity<Product> put(@Valid @RequestBody Product product){
        return productRepository.findById(product.getId())
                .map(resposta -> ResponseEntity.status(HttpStatus.OK)
                        .body(productRepository.save(product)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // @PatchMapping

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);

        if(product.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        productRepository.deleteById(id);
    }


}