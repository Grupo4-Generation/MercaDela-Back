package com.generation.mercadela.model;

import lombok.Data;

@Data
public class UserLogin {
    private Long id;
    private String cpf;
    private String name;
    private String email;
    private String password;
    private String gender;
    private String photo;
    private String token;
}
