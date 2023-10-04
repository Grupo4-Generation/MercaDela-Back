package com.generation.mercadela.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "nome não pode ser nulo")
    @Size(max = 255, message = "nome não pode ultrapassar 255 caracteres")
    private String nome;
    @NotBlank(message = "email não pode ser nulo")
    @Size(max = 255, message = "email não pode ultrapassar 255 caracteres")
    private String email;

    private Integer tipoUsuario;
    @NotBlank(message = "senha não pode ser nulo")
    @Size(max = 255, message = "senha não pode ultrapassar 255 caracteres")
    private String senha;
    @NotBlank(message = "genero não pode ser nulo")
    @Size(max = 255, message = "genero não pode ultrapassar 255 caracteres")
    private String genero;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Integer tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
