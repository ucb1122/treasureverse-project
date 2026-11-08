package com.treasureverse.backend.sale.domain;

import java.util.List;
import java.util.Optional;

public interface SalePostRepositoryPort {
  SalePost save(SalePost salePost);

  Optional<SalePost> findById(Long id);

  List<SalePost> findAll();

  void delete(SalePost salePost);
}
