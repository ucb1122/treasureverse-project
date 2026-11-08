package com.treasureverse.backend.sale.infrastructure;

import com.treasureverse.backend.sale.domain.SalePost;
import com.treasureverse.backend.sale.domain.SalePostRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SalePostRepositoryAdapter implements SalePostRepositoryPort {
  private final SalePostJpaRepository repository;

  public SalePostRepositoryAdapter(SalePostJpaRepository repository) {
    this.repository = repository;
  }

  public SalePost save(SalePost post) {
    return repository.save(post);
  }

  public Optional<SalePost> findById(Long id) {
    return repository.findById(id);
  }

  public List<SalePost> findAll() {
    return repository.findAll();
  }

  public void delete(SalePost post) {
    repository.delete(post);
  }
}
