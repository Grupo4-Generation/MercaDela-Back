package com.generation.mercadela.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLogin {

    @Schema(hidden = true)
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
    private String gender;

    @Schema(hidden = true)
    private String photo;

    @Schema(hidden = true)
    private boolean isAdmin;

    @Schema(hidden = true)
    private String token;
}
