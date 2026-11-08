package com.treasureverse.backend.purchase.infrastructure;

import com.treasureverse.backend.purchase.domain.PurchasePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasePostJpaRepository extends JpaRepository<PurchasePost, Long> {}
