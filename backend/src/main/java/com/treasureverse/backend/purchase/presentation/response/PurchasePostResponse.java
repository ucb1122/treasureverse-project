package com.treasureverse.backend.purchase.presentation.response;

import com.treasureverse.backend.purchase.domain.PurchasePost;
import com.treasureverse.backend.purchase.domain.PurchaseStatus;
import java.math.BigDecimal;

public record PurchasePostResponse(
    Long id, String title, BigDecimal desiredPrice, PurchaseStatus status) {
  public static PurchasePostResponse from(PurchasePost purchasePost) {
    return new PurchasePostResponse(
        purchasePost.id(), purchasePost.title(), purchasePost.price(), purchasePost.status());
  }
}
