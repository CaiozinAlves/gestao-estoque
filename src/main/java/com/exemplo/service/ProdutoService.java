package com.exemplo.service;

import com.exemplo.model.Produto;
import com.exemplo.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Integrante A — Teste de Unidade
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
