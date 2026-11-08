package com.treasureverse.backend.purchase.infrastructure;

import com.treasureverse.backend.purchase.domain.PurchasePost;
import com.treasureverse.backend.purchase.domain.PurchasePostRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PurchasePostRepositoryAdapter implements PurchasePostRepositoryPort {
  private final PurchasePostJpaRepository repository;

  public PurchasePostRepositoryAdapter(PurchasePostJpaRepository repository) {
    this.repository = repository;
  }

  public PurchasePost save(PurchasePost post) {
    return repository.save(post);
  }

  public Optional<PurchasePost> findById(Long id) {
    return repository.findById(id);
  }

  public List<PurchasePost> findAll() {
    return repository.findAll();
  }

  public void delete(PurchasePost post) {
    repository.delete(post);
  }
}
