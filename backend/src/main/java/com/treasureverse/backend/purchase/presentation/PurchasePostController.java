package com.treasureverse.backend.purchase.presentation;

import com.treasureverse.backend.common.exception.*;
import com.treasureverse.backend.common.response.ApiResponse;
import com.treasureverse.backend.purchase.application.PurchasePostService;
import com.treasureverse.backend.purchase.domain.PurchasePost;
import com.treasureverse.backend.purchase.domain.PurchaseStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/purchase-posts")
public class PurchasePostController {
  private final PurchasePostService purchasePostService;

  public PurchasePostController(PurchasePostService purchasePostService) {
    this.purchasePostService = purchasePostService;
  }

  record Req(@NotBlank String title, @NotBlank String content, @Positive BigDecimal desiredPrice) {}

  record Update(
      @NotBlank String title,
      @NotBlank String content,
      @Positive BigDecimal desiredPrice,
      @NotNull PurchaseStatus status) {}

  record View(Long id, String title, BigDecimal desiredPrice, PurchaseStatus status) {}

  View view(PurchasePost p) {
    return new View(p.id(), p.title(), p.price(), p.status());
  }

  PurchasePost post(Long id) {
    return purchasePostService.findById(id);
  }

  void owner(PurchasePost p, String u) {
    if (!p.buyerId().equals(Long.valueOf(u))) throw new BusinessException(ErrorCode.CONFLICT);
  }

  @PostMapping
  public ApiResponse<View> create(@Valid @RequestBody Req r, @AuthenticationPrincipal String u) {
    var p = purchasePostService.create(Long.valueOf(u), r.title(), r.content(), r.desiredPrice());
    return ApiResponse.success(view(p));
  }

  @GetMapping
  public ApiResponse<List<View>> list() {
    return ApiResponse.success(purchasePostService.findAll().stream().map(this::view).toList());
  }

  @GetMapping("/{id}")
  public ApiResponse<View> get(@PathVariable Long id) {
    return ApiResponse.success(view(post(id)));
  }

  @PutMapping("/{id}")
  public ApiResponse<View> update(
      @PathVariable Long id, @Valid @RequestBody Update r, @AuthenticationPrincipal String u) {
    var p =
        purchasePostService.update(
            id, Long.valueOf(u), r.title(), r.content(), r.desiredPrice(), r.status());
    return ApiResponse.success(view(p));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal String u) {
    purchasePostService.delete(id, Long.valueOf(u));
    return ResponseEntity.noContent().build();
  }
}
