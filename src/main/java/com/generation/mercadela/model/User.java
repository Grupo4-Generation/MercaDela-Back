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

    @Schema(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(example = "email@email.com.br")
    @NotNull
    private String email;

    @NotBlank
    @Size(min = 8, max = 255)
    private String password;

    @Schema(hidden = true)
    private String cpf;

    @Schema(hidden = true)
    private String name;

    @Schema(hidden = true)
    @Size(max = 255)
    private String gender;

    @Schema(hidden = true)
    @Size(max = 5000)
    private String photo;

    @Schema(hidden = true)
    private boolean isAdmin;

    @Schema(hidden = true)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties("user")
    private List<Product> product;

}
