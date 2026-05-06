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
    @DisplayName("POST /produtos deve retornar 201 ao cadastrar produto completo e válido")
    void deveCadastrarProdutoERetornar201() throws Exception {
        Produto entrada = new Produto(null, "Notebook", 3499.00, "Eletrônicos", 10, "NTBK3499");
        Produto saida   = new Produto(1L,   "Notebook", 3499.00, "Eletrônicos", 10, "NTBK3499");
        when(service.cadastrar(any())).thenReturn(saida);

        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entrada)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.sku").value("NTBK3499"))
            .andExpect(jsonPath("$.quantidade").value(10));
    }

    @Test
    @DisplayName("POST /produtos deve retornar 400 Bad Request se a quantidade for negativa (Validação do Controller)")
    void deveRetornar400QuandoQuantidadeNegativa() throws Exception {
        // Quantidade -5 viola a anotação @Min(0) na entidade Produto
        Produto invalido = new Produto(null, "Celular", 1500.0, "Eletrônicos", -5, "CEL15000");

        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalido)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.quantidade").value("A quantidade não pode ser negativa"));
    }

    @Test
    @DisplayName("POST /produtos deve retornar 400 Bad Request quando a regra de SKU duplicado for violada pelo Service")
    void deveRetornar400QuandoSkuDuplicado() throws Exception {
        Produto entrada = new Produto(null, "Notebook", 3499.00, "Eletrônicos", 10, "NTBK3499");
        
        // O Service lança a exceção manual ao detectar duplicação
        when(service.cadastrar(any())).thenThrow(new IllegalArgumentException("Já existe um produto cadastrado com este SKU."));

        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entrada)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.erro").value("Já existe um produto cadastrado com este SKU."));
    }
    @Test
    @DisplayName("GET /produtos deve retornar lista de produtos com status 200")
    void deveListarProdutosERetornar200() throws Exception {
        List<Produto> lista = List.of(
            new Produto(1L, "Camiseta", 49.90, "Vestuário", 10, "SKU12345"),
            new Produto(2L, "Calça",    129.90, "Vestuário", 5, "SKU54321")
        );
        when(service.listarTodos()).thenReturn(lista);

        mockMvc.perform(get("/produtos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].nome").value("Camiseta"));
    }

    @Test
    @DisplayName("POST /produtos com nome vazio deve retornar 400 Bad Request")
    void deveRetornar400QuandoNomeVazio() throws Exception {
        Produto invalido = new Produto(null, "", 50.0, "Vestuário", 10, "SKU12345");

        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalido)))
            .andExpect(status().isBadRequest());
    }
}
