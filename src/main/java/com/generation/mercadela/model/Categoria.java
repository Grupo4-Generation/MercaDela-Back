package com.generation.mercadela.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "tb_categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "NomeCategoria não pode ser nulo.")
    @Size(min = 2, max = 255, message = "NomeCategoria não pode ser menor que 2 e ultrapassar 255 caracteres.")
    private String nomeCategoria;

    @NotBlank(message = "DescricaoCategoria não pode ser nulo.")
    @Size(min = 10, max = 255, message = "DescricaoCategoria não pode ser menor que 10 e ultrapassar 255 caracteres.")
    private String descricaoCategoria;

    @OneToMany(mappedBy = "idCategoria", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties("idCategoria")
    private List<Produto> produtos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public String getDescricaoCategoria() {
        return descricaoCategoria;
    }

    public void setDescricaoCategoria(String descricaoCategoria) {
        this.descricaoCategoria = descricaoCategoria;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
}
