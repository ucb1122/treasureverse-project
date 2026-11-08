package com.treasureverse.backend.purchase.domain;

import java.util.List;
import java.util.Optional;

public interface PurchasePostRepositoryPort {
  PurchasePost save(PurchasePost purchasePost);

  Optional<PurchasePost> findById(Long id);

  List<PurchasePost> findAll();

  void delete(PurchasePost purchasePost);
}
