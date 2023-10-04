package com.generation.mercadela.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="tb_produtos")
public class Produtos {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	private Long idCategoria;
	
	@NotBlank(message = "NomeProduto não pode ser nulo.")
	@Size(min=2,max=255,message = "NomeProduto não pode ser menor que 2 e ultrapassar 255 caracteres.")
	private String nomeProduto;
	
	@NotBlank(message = "DescriçãoProduto não pode ser nulo.")
	@Size(min=10,max=255,message = "DescriçãoProduto não pode ser menor que 10 e ultrapassar 255 caracteres.")
	private String descricaoProduto;
	
	@NotNull(message = "PreçoProduto não pode ser nulo.")
	@Positive(message = "PreçoProduto deve ser positivo.")
	@Size(max=10,message = "PrecoProduto não pode ultrapassar 10 caracteres.")
	private BigDecimal precoProduto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Long idCategoria) {
		this.idCategoria = idCategoria;
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
}