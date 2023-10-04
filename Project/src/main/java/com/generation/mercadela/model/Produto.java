package com.generation.mercadela.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="tb_produtos")
public class Produto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	@NotBlank(message = "NomeProduto não pode ser nulo.")
	@Size(min=2,max=255,message = "NomeProduto não pode ser menor que 2 e ultrapassar 255 caracteres.")
	private String nomeProduto;
	
	@NotBlank(message = "DescricaoProduto não pode ser nulo.")
	@Size(min=10,max=255,message = "DescricaoProduto não pode ser menor que 10 e ultrapassar 255 caracteres.")
	private String descricaoProduto;
	
	@NotNull(message = "PrecoProduto não pode ser nulo.")
	@Positive(message = "PrecoProduto deve ser positivo.")
	@Size(max=10,message = "PrecoProduto não pode ultrapassar 10 caracteres.")
	private BigDecimal precoProduto;

	@ManyToOne
	@JsonIgnoreProperties("produtos")
    private Categoria idCategoria;

	@ManyToOne
	@JsonIgnoreProperties("produtos")
	private Usuario idUsuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public BigDecimal getPrecoProduto() {
		return precoProduto;
	}

	public void setPrecoProduto(BigDecimal precoProduto) {
		this.precoProduto = precoProduto;
	}

	public Categoria getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Categoria idCategoria) {
		this.idCategoria = idCategoria;
	}

	public Usuario getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}
}