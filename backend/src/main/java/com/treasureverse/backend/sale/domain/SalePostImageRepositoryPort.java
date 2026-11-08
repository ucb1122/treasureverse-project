package com.treasureverse.backend.sale.domain;

public interface SalePostImageRepositoryPort {
  long countBySalePostId(Long salePostId);

  SalePostImage save(SalePostImage image);
}
