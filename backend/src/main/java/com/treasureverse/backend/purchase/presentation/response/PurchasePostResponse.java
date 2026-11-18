package com.treasureverse.backend.purchase.presentation.response;

import com.treasureverse.backend.purchase.domain.PurchasePost;
import com.treasureverse.backend.purchase.domain.PurchaseStatus;
import java.math.BigDecimal;
import java.util.List;

public record PurchasePostResponse(
    Long id,
    String title,
    String content,
    BigDecimal desiredPrice,
    PurchaseStatus status,
    List<String> imageUrls) {
  public static PurchasePostResponse from(PurchasePost purchasePost, List<String> imageUrls) {
    return new PurchasePostResponse(
        purchasePost.id(),
        purchasePost.title(),
        purchasePost.content(),
        purchasePost.price(),
        purchasePost.status(),
        imageUrls);
  }
}
