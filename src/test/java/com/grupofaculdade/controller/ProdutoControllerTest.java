package com.grupofaculdade.controller;

import com.grupofaculdade.model.Produto;
import com.grupofaculdade.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("HU-01: POST /produtos deve retornar 201 ao cadastrar produto válido")
    void deveCadastrarProdutoERetornar201() throws Exception {
        // Arrange
        Produto entrada = new Produto(null, "Notebook", 3499.00);
        Produto saida   = new Produto(1L,   "Notebook", 3499.00);
        when(service.cadastrar(any())).thenReturn(saida);

        // Act & Assert
        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entrada)))
            .andExpect(status().isCreated())              // 201
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nome").value("Notebook"))
            .andExpect(jsonPath("$.preco").value(3499.00));
    }

    @Test
    @DisplayName("GET /produtos deve retornar lista de produtos com status 200")
    void deveListarProdutosERetornar200() throws Exception {
        // Arrange
        List<Produto> lista = List.of(
            new Produto(1L, "Camiseta", 49.90),
            new Produto(2L, "Calça",    129.90)
        );
        when(service.listarTodos()).thenReturn(lista);

        // Act & Assert
        mockMvc.perform(get("/produtos"))
            .andExpect(status().isOk())                   // 200
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].nome").value("Camiseta"));
    }

    @Test
    @DisplayName("POST /produtos com nome vazio deve retornar 400 Bad Request")
    void deveRetornar400QuandoNomeVazio() throws Exception {
        // Arrange — nome em branco (violação de @NotBlank)
        Produto invalido = new Produto(null, "", 50.0);

        // Act & Assert
        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalido)))
            .andExpect(status().isBadRequest());           // 400
    }
}
