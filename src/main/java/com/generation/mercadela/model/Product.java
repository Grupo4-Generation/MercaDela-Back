package com.generation.mercadela.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

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
    @PositiveOrZero
    private BigDecimal price;

    @NotBlank
    private String photo;

    @ManyToOne
    @JsonIgnoreProperties("products") // Ignore products in Category
    @NotNull
    private Category category;

    @ManyToOne
    @JsonIgnore // Ignora o user ao serializar o Product
    private User user;
}
