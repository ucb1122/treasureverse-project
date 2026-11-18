package com.treasureverse.backend.sale.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SalePostRepositoryPort {
  SalePost save(SalePost salePost);

  Optional<SalePost> findById(Long id);

  List<SalePost> findAll();

  Page<SalePost> findAll(Pageable pageable);

  void delete(SalePost salePost);
}
