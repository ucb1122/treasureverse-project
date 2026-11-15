package com.treasureverse.backend.sale.presentation.response;

import com.treasureverse.backend.sale.domain.SalePost;
import com.treasureverse.backend.sale.domain.SaleStatus;
import java.math.BigDecimal;
import java.util.List;

public record SalePostResponse(
    Long id,
    String title,
    String content,
    BigDecimal price,
    SaleStatus status,
    List<String> imageUrls) {
  public static SalePostResponse from(SalePost salePost, List<String> imageUrls) {
    return new SalePostResponse(
        salePost.getId(),
        salePost.getTitle(),
        salePost.getContent(),
        salePost.getPrice(),
        salePost.getStatus(),
        imageUrls);
  }
}
