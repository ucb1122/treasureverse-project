package com.treasureverse.backend.sale.presentation.response;

import com.treasureverse.backend.sale.domain.SalePost;
import com.treasureverse.backend.sale.domain.SaleStatus;
import java.math.BigDecimal;

public record SalePostResponse(Long id, String title, BigDecimal price, SaleStatus status) {
  public static SalePostResponse from(SalePost salePost) {
    return new SalePostResponse(
        salePost.getId(), salePost.getTitle(), salePost.getPrice(), salePost.getStatus());
  }
}
