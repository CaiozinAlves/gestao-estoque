package com.exemplo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser positivo")
    @Column(nullable = false)
    private Double preco;
<<<<<<< Updated upstream:src/main/java/com/exemplo/model/Produto.java
=======

    @NotBlank(message = "A categoria é obrigatória")
    @Column(nullable = false)
    private String categoria;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 0, message = "A quantidade não pode ser negativa")
    @Column(nullable = false)
    private Integer quantidade;

    @NotBlank(message = "O SKU é obrigatório")
    @Size(min = 8, max = 8, message = "O SKU deve ter exatamente 8 caracteres")
    @Column(nullable = false, unique = true)
    private String sku;

    public Produto() {
    }

    public Produto(Long id, String nome, Double preco, String categoria, Integer quantidade, String sku) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
        this.quantidade = quantidade;
        this.sku = sku;
    }

    public static ProdutoBuilder builder() {
        return new ProdutoBuilder();
    }

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

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public static class ProdutoBuilder {
        private Long id;
        private String nome;
        private Double preco;
        private String categoria;
        private Integer quantidade;
        private String sku;

        public ProdutoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProdutoBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public ProdutoBuilder preco(Double preco) {
            this.preco = preco;
            return this;
        }

        public ProdutoBuilder categoria(String categoria) {
            this.categoria = categoria;
            return this;
        }

        public ProdutoBuilder quantidade(Integer quantidade) {
            this.quantidade = quantidade;
            return this;
        }

        public ProdutoBuilder sku(String sku) {
            this.sku = sku;
            return this;
        }

        public Produto build() {
            return new Produto(id, nome, preco, categoria, quantidade, sku);
        }
    }
>>>>>>> Stashed changes:src/main/java/com/grupofaculdade/model/Produto.java
}
