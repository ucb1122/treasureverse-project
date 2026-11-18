package com.treasureverse.backend.purchase.domain;

import java.util.List;

public interface PurchasePostImageRepositoryPort {
  long countByPostId(Long postId);

  List<PurchasePostImage> findByPostId(Long postId);

  PurchasePostImage save(PurchasePostImage image);
}
