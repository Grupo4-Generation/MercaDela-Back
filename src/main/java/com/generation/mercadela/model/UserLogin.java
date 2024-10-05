package com.generation.mercadela.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserLogin {
    
    @Schema(example = "email@email.com.br")
    private String email;
    private String password;

    @Schema(hidden = true) // Retirar atributo na leitura do swagger apenas para não causar erro de produtos inexistentes quando criar novos usuários
    private Long id;

    @Schema(hidden = true) // Retirar atributo na leitura do swagger apenas para não causar erro de produtos inexistentes quando criar novos usuários
    private String cpf;

    @Schema(hidden = true) // Retirar atributo na leitura do swagger apenas para não causar erro de produtos inexistentes quando criar novos usuários
    private String name;

    @Schema(hidden = true) // Retirar atributo na leitura do swagger apenas para não causar erro de produtos inexistentes quando criar novos usuários
    private String gender;
    
    @Schema(hidden = true) // Retirar atributo na leitura do swagger apenas para não causar erro de produtos inexistentes quando criar novos usuários
    private String photo;
    
    @Schema(hidden = true) // Retirar atributo na leitura do swagger apenas para não causar erro de produtos inexistentes quando criar novos usuários
    private String token;
}
