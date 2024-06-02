package com.generation.mercadela.controller;

import com.generation.mercadela.model.Category;
import com.generation.mercadela.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoryController {

    @Autowired
    private CategoryRepository categoriaRepository;

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoriaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        return categoriaRepository.findById(id)
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("name/{name}")
    public ResponseEntity<List<Category>> getByname(@PathVariable String nomeCategoria){
        return ResponseEntity.ok(categoriaRepository.findAllByNameContainingIgnoreCase(nomeCategoria));
    }

    @PutMapping
    public ResponseEntity<Category> put(@Valid @RequestBody Category categoria){
        return categoriaRepository.findById(categoria.getId())
                .map(resposta -> ResponseEntity.status(HttpStatus.OK)
                        .body(categoriaRepository.save(categoria)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Category> post(@Valid @RequestBody Category categoria){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaRepository.save(categoria));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Category> categoria = categoriaRepository.findById(id);

        if(categoria.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        categoriaRepository.deleteById(id);
    }
}
