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
    @DisplayName("Deve salvar produto completo e recuperar pelo ID")
    void deveSalvarERecuperarProduto() {
        Produto produto = Produto.builder()
                .nome("Notebook Gamer")
                .preco(4500.0)
                .categoria("Eletrônicos")
                .quantidade(5)
                .sku("NOTEB001")
                .build();

        Produto salvo = repository.save(produto);
        Optional<Produto> encontrado = repository.findById(salvo.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("Notebook Gamer", encontrado.get().getNome());
        assertEquals("NOTEB001", encontrado.get().getSku());
    }

    @Test
    @DisplayName("Deve conseguir buscar um produto especificamente pelo seu SKU")
    void deveBuscarProdutoPorSku() {
        repository.save(Produto.builder()
            .nome("Mouse")
            .preco(150.0)
            .categoria("Periféricos")
            .quantidade(20)
            .sku("MOUSE123")
            .build());

        Optional<Produto> encontrado = repository.findBySku("MOUSE123");

        assertTrue(encontrado.isPresent());
        assertEquals("Mouse", encontrado.get().getNome());
    }
    @Test
    @DisplayName("REGRESSÃO: findAll deve retornar todos os produtos salvos")
    void deveListarTodosProdutosSalvos() {
        repository.save(Produto.builder().nome("Produto A").preco(10.0).categoria("Cat").quantidade(1).sku("SKU00001").build());
        repository.save(Produto.builder().nome("Produto B").preco(20.0).categoria("Cat").quantidade(1).sku("SKU00002").build());
        repository.save(Produto.builder().nome("Produto C").preco(30.0).categoria("Cat").quantidade(1).sku("SKU00003").build());

        List<Produto> produtos = repository.findAll();

        assertEquals(3, produtos.size());
    }

    @Test
    @DisplayName("REGRESSÃO: Deve deletar produto e confirmar remoção")
    void deveDeletarProduto() {
        Produto produto = repository.save(Produto.builder().nome("Tênis").preco(199.90).categoria("Cat").quantidade(1).sku("SKU00004").build());
        Long id = produto.getId();

        repository.deleteById(id);

        assertFalse(repository.findById(id).isPresent());
    }
}
