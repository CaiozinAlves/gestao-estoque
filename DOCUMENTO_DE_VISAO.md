# DOCUMENTO DE VISÃO — Sistema de Gestão de Estoque

## 1. O Problema
O controle manual de produtos em estoque gera erros de cadastro, inconsistências de preço e dificuldade no rastreamento de itens. Sem validação automatizada, dados incorretos (preços negativos, nomes em branco) são inseridos com frequência.

## 2. A Solução
Um sistema automatizado de cadastro de produtos via API REST, desenvolvido com Spring Boot, que garante a integridade dos dados por meio de validações de negócio e testes automatizados em três camadas.

## 3. História de Usuário Principal

**HU-01:** *"Como administrador, quero cadastrar produtos com nome e preço para manter o estoque atualizado."*

**Critérios de Aceite:**
- O sistema deve aceitar produtos com nome (2–100 caracteres) e preço positivo.
- O sistema deve rejeitar preços negativos ou iguais a zero.
- O sistema deve rejeitar nomes em branco.
- O endpoint deve retornar HTTP 201 ao cadastrar com sucesso.

## 4. Arquitetura

```
ProdutoController  (camada HTTP)
      ↓
ProdutoService     (camada de negócio — validações)
      ↓
ProdutoRepository  (camada de dados — JPA + H2)
      ↓
Banco H2 (em memória)
```

## 5. Tabela de Testes

| Tipo         | Camada      | Cenário                                      | Ferramenta         |
|:-------------|:------------|:---------------------------------------------|:-------------------|
| Unidade      | Service     | Preço negativo deve lançar exceção            | JUnit 5 + Mockito  |
| Unidade      | Service     | Nome em branco deve lançar exceção            | JUnit 5 + Mockito  |
| Integração   | Repository  | Salvar e recuperar produto pelo ID no H2      | @DataJpaTest       |
| Regressão    | Repository  | findAll retorna todos os produtos persistidos | @DataJpaTest       |
| Sistema      | Controller  | POST /produtos retorna 201 Created            | MockMvc            |
| Sistema      | Controller  | POST inválido retorna 400 Bad Request         | MockMvc            |

## 6. Como Executar

```bash
# Clonar o repositório
git clone <url-do-repositorio>
cd gestao-estoque

# Rodar os testes
# No Linux/Mac:
./mvnw test
# No Windows (PowerShell/CMD):
.\mvnw.cmd test

# Subir a aplicação
# No Linux/Mac:
./mvnw spring-boot:run
# No Windows:
.\mvnw.cmd spring-boot:run
# API disponível em: http://localhost:8080/produtos
```
