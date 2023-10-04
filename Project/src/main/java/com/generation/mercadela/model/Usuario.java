package com.generation.mercadela.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*; 
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "NomeUsuario não pode ser nulo.")
    @Size(min=2,max = 255, message = "NomeUsuario não pode ser menor que 2 e ultrapassar 255 caracteres.")
    private String nomeUsuario;
    
    @NotBlank(message = "NomeEmail não pode ser nulo.")
    @Size(max = 255, message = "NomeEmail não pode ultrapassar 255 caracteres.")
    private String emailUsuario;

    private Integer tipoUsuario;
    
    @NotBlank(message = "SenhaUsuario não pode ser nulo.")
    @Size(min=8,max = 255, message = "SenhaUsuario não pode ser menor que 8 e ultrapassar 255 caracteres.")
    private String senhaUsuario;
    
    @NotBlank(message = "GeneroUsuario não pode ser nulo.")
    @Size(max = 255, message = "GeneroUsuario não pode ultrapassar 255 caracteres.")
    private String generoUsuario;

	@OneToMany(mappedBy = "idUsuario", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties("idUsuario")
	private List<Produto> produtos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public Integer getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(Integer tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public String getSenhaUsuario() {
		return senhaUsuario;
	}

	public void setSenhaUsuario(String senhaUsuario) {
		this.senhaUsuario = senhaUsuario;
	}

	public String getGeneroUsuario() {
		return generoUsuario;
	}

	public void setGeneroUsuario(String generoUsuario) {
		this.generoUsuario = generoUsuario;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}
}
