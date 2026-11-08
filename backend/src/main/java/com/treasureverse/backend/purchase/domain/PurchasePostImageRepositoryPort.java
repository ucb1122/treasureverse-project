package com.treasureverse.backend.purchase.domain;

public interface PurchasePostImageRepositoryPort {
  long countByPostId(Long postId);

  PurchasePostImage save(PurchasePostImage image);
}
