package com.grupofaculdade.service;

import com.grupofaculdade.model.Produto;
import com.grupofaculdade.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService service;

    @Mock
    private ProdutoRepository repository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve cadastrar produto com todos os dados válidos")
    void deveCadastrarProdutoComDadosValidos() {
        Produto produto = new Produto(null, "Camiseta", 49.90, "Vestuário", 10, "SKU12345");
        Produto produtoSalvo = new Produto(1L, "Camiseta", 49.90, "Vestuário", 10, "SKU12345");
        
        when(repository.findBySku("SKU12345")).thenReturn(Optional.empty());
        when(repository.save(produto)).thenReturn(produtoSalvo);

        Produto resultado = service.cadastrar(produto);

        assertNotNull(resultado.getId());
        assertEquals("SKU12345", resultado.getSku());
        verify(repository, times(1)).save(produto);
    }

    @Test
    @DisplayName("Deve rejeitar produto com preço negativo")
    void deveRejeitarProdutoComPrecoNegativo() {
        Produto produto = new Produto(null, "Camiseta", -10.0, "Vestuário", 10, "SKU12345");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.cadastrar(produto));
        assertEquals("O preço do produto deve ser positivo.", ex.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar produto com quantidade negativa")
    void deveRejeitarProdutoComQuantidadeNegativa() {
        Produto produto = new Produto(null, "Camiseta", 49.90, "Vestuário", -5, "SKU12345");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.cadastrar(produto));
        assertEquals("A quantidade não pode ser negativa.", ex.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar produto se o SKU já estiver cadastrado no banco")
    void deveRejeitarProdutoComSkuDuplicado() {
        Produto novoProduto = new Produto(null, "Camiseta Azul", 50.0, "Vestuário", 10, "SKUDUPLO");
        Produto produtoExistente = new Produto(1L, "Camiseta Vermelha", 45.0, "Vestuário", 5, "SKUDUPLO");

        // Simula que o repositório já encontrou um produto com esse SKU
        when(repository.findBySku("SKUDUPLO")).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.cadastrar(novoProduto));
        assertEquals("Já existe um produto cadastrado com este SKU.", ex.getMessage());
        
        // Garante que o método save nunca foi chamado
        verify(repository, never()).save(any());
    }
}
