package com.treasureverse.backend.sale.infrastructure;

import com.treasureverse.backend.sale.domain.SalePostImage;
import com.treasureverse.backend.sale.domain.SalePostImageRepositoryPort;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SalePostImageRepositoryAdapter implements SalePostImageRepositoryPort {
  private final SalePostImageJpaRepository repository;

  public SalePostImageRepositoryAdapter(SalePostImageJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public long countBySalePostId(Long salePostId) {
    return repository.countBySalePostId(salePostId);
  }

  @Override
  public SalePostImage save(SalePostImage image) {
    return repository.save(image);
  }

  @Override
  public List<SalePostImage> findBySalePostId(Long salePostId) {
    return repository.findBySalePostIdOrderBySortOrderAsc(salePostId);
  }
}
