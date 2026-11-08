package com.treasureverse.backend.purchase.infrastructure;

import com.treasureverse.backend.purchase.domain.PurchasePostImage;
import com.treasureverse.backend.purchase.domain.PurchasePostImageRepositoryPort;
import org.springframework.stereotype.Repository;

@Repository
public class PurchasePostImageRepositoryAdapter implements PurchasePostImageRepositoryPort {
  private final PurchasePostImageJpaRepository repository;

  public PurchasePostImageRepositoryAdapter(PurchasePostImageJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public long countByPostId(Long postId) {
    return repository.countByPostId(postId);
  }

  @Override
  public PurchasePostImage save(PurchasePostImage image) {
    return repository.save(image);
  }
}
