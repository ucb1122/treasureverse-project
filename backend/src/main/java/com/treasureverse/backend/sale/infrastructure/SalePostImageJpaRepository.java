package com.treasureverse.backend.sale.infrastructure;

import com.treasureverse.backend.sale.domain.SalePostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalePostImageJpaRepository extends JpaRepository<SalePostImage, Long> {
  long countBySalePostId(Long salePostId);
}
