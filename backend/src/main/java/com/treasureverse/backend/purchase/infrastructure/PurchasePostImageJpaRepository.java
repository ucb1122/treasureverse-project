package com.treasureverse.backend.purchase.infrastructure;

import com.treasureverse.backend.purchase.domain.PurchasePostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasePostImageJpaRepository extends JpaRepository<PurchasePostImage, Long> {
  long countByPostId(Long postId);
}
