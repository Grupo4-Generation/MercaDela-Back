package com.generation.mercadela.dto;

import java.util.List;

import com.generation.mercadela.model.Product;

import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;

    private String email;

    private String password;

    private boolean isAdmin;

    private String cpf;

    private String name;

    private String gender;

    private String photo;

    private List<Product> product;
}
