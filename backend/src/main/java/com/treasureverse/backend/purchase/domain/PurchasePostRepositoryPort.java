package com.treasureverse.backend.purchase.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchasePostRepositoryPort {
  PurchasePost save(PurchasePost purchasePost);

  Optional<PurchasePost> findById(Long id);

  List<PurchasePost> findAll();

  Page<PurchasePost> findAll(Pageable pageable);

  void delete(PurchasePost purchasePost);
}
