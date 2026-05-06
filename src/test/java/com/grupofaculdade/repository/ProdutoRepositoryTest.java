package com.grupofaculdade.repository;

import com.grupofaculdade.model.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository repository;

    @Test
    @DisplayName("Deve salvar produto e recuperar pelo ID")
    void deveSalvarERecuperarProduto() {
        // Arrange
        Produto produto = Produto.builder()
                .nome("Calça Jeans")
                .preco(129.90)
                .build();

        // Act
        Produto salvo = repository.save(produto);
        Optional<Produto> encontrado = repository.findById(salvo.getId());

        // Assert
        assertTrue(encontrado.isPresent());
        assertEquals("Calça Jeans", encontrado.get().getNome());
        assertEquals(129.90, encontrado.get().getPreco());
    }

    @Test
    @DisplayName("REGRESSÃO: findAll deve retornar todos os produtos salvos")
    void deveListarTodosProdutosSalvos() {
        // Arrange
        repository.save(Produto.builder().nome("Produto A").preco(10.0).build());
        repository.save(Produto.builder().nome("Produto B").preco(20.0).build());
        repository.save(Produto.builder().nome("Produto C").preco(30.0).build());

        // Act
        List<Produto> produtos = repository.findAll();

        // Assert
        assertEquals(3, produtos.size());
    }

    @Test
    @DisplayName("REGRESSÃO: Deve deletar produto e confirmar remoção")
    void deveDeletarProduto() {
        // Arrange
        Produto produto = repository.save(
            Produto.builder().nome("Tênis").preco(199.90).build()
        );
        Long id = produto.getId();

        // Act
        repository.deleteById(id);

        // Assert
        assertFalse(repository.findById(id).isPresent());
    }
}
