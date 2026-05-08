package com.grupofaculdade.repository;

import com.grupofaculdade.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByNomeContainingIgnoreCase(String nome);
    
    // Nova regra de negócio para a apresentação: SKU único
    Optional<Produto> findBySku(String sku);
}
