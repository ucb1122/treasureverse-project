package com.treasureverse.backend.purchase;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasePostImageRepository extends JpaRepository<PurchasePostImage, Long> {
  long countByPostId(Long postId);
}
