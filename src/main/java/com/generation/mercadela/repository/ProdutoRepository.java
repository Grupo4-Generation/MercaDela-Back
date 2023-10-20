package com.generation.mercadela.repository;

import com.generation.mercadela.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto,Long> {
     List<Produto> findByNomeProdutoContainingIgnoreCase(@Param("nomeProduto")String nomeProduto);
}
