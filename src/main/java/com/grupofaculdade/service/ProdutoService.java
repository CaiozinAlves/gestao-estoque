package com.grupofaculdade.service;

import com.grupofaculdade.model.Produto;
import com.grupofaculdade.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    public Produto cadastrar(Produto produto) {
        if (produto.getPreco() == null || produto.getPreco() <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser positivo.");
        }
        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        if (produto.getQuantidade() == null || produto.getQuantidade() < 0) {
            throw new IllegalArgumentException("A quantidade não pode ser negativa.");
        }
        if (produto.getSku() == null || produto.getSku().isBlank()) {
            throw new IllegalArgumentException("O SKU do produto é obrigatório.");
        }
        
        // Regra de negócio forte: O SKU deve ser único no banco de dados
        if (repository.findBySku(produto.getSku()).isPresent()) {
            throw new IllegalArgumentException("Já existe um produto cadastrado com este SKU.");
        }

        return repository.save(produto);
    }

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + id));
    }

    public void deletar(Long id) {
        Produto produto = buscarPorId(id);
        repository.delete(produto);
    }
}
