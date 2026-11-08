package com.treasureverse.backend.sale.infrastructure;

import com.treasureverse.backend.sale.domain.SalePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalePostJpaRepository extends JpaRepository<SalePost, Long> {}
