package com.generation.mercadela.repository;

import com.generation.mercadela.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
     List<Product> findByNameContainingIgnoreCase(@Param("nome")String nome);
}
