package com.exemplo.service;

import com.exemplo.model.Produto;
import com.exemplo.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// ============================================================
// INTEGRANTE A — TESTE DE UNIDADE
// Valida a lógica de negócio do ProdutoService de forma isolada
// ============================================================
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
    @DisplayName("HU-01: Deve cadastrar produto com dados válidos")
    void deveCadastrarProdutoComDadosValidos() {
        // Arrange
        Produto produto = new Produto(null, "Camiseta", 49.90);
        Produto produtoSalvo = new Produto(1L, "Camiseta", 49.90);
        when(repository.save(produto)).thenReturn(produtoSalvo);

        // Act
        Produto resultado = service.cadastrar(produto);

        // Assert
        assertNotNull(resultado.getId());
        assertEquals("Camiseta", resultado.getNome());
        assertEquals(49.90, resultado.getPreco());
        verify(repository, times(1)).save(produto);
    }

    @Test
    @DisplayName("HU-01: Deve rejeitar produto com preço negativo")
    void deveRejeitarProdutoComPrecoNegativo() {
        // Arrange
        Produto produto = new Produto(null, "Produto Inválido", -1.00);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.cadastrar(produto)
        );
        assertEquals("O preço do produto deve ser positivo.", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("HU-01: Deve rejeitar produto com preço zero")
    void deveRejeitarProdutoComPrecoZero() {
        Produto produto = new Produto(null, "Produto Zero", 0.0);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(produto));
    }

    @Test
    @DisplayName("HU-01: Deve rejeitar produto com nome em branco")
    void deveRejeitarProdutoComNomeEmBranco() {
        Produto produto = new Produto(null, "   ", 50.0);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.cadastrar(produto)
        );
        assertEquals("O nome do produto é obrigatório.", ex.getMessage());
    }
}
