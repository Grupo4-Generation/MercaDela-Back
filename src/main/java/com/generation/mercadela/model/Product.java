package com.generation.mercadela.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tb_products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 255)
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @PositiveOrZero()
    private BigDecimal price;

    @NotBlank
    private String photo;

    @ManyToOne
    @JsonIgnoreProperties("product")
    @NotNull
    private Category category;

    @ManyToOne
    @JsonIgnoreProperties("product")
    private User user;

}
