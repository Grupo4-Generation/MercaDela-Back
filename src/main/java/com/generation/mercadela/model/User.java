package com.generation.mercadela.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cpf;

    private String name;

    @Schema(example = "email@email.com.br")
    @NotNull
    private String email;

    @NotBlank
    @Size(min = 8, max = 255)
    private String password;

    @Size(max = 5000)
    private String photo;

    @Size(max = 255)
    private String gender;

    private boolean admin;

    @Schema(hidden = true) // Retirar atributo na leitura do swagger apenas para não causar erro de produtos inexistentes quando criar novos usuários
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties("user")
    private List<Product> product;

}
