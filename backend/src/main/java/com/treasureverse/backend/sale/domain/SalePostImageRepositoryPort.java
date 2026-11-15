package com.treasureverse.backend.sale.domain;

import java.util.List;

public interface SalePostImageRepositoryPort {
  long countBySalePostId(Long salePostId);

  SalePostImage save(SalePostImage image);

  List<SalePostImage> findBySalePostId(Long salePostId);
}
