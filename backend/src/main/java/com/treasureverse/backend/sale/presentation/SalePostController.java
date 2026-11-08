package com.treasureverse.backend.sale.presentation;

import com.treasureverse.backend.common.exception.*;
import com.treasureverse.backend.common.response.ApiResponse;
import com.treasureverse.backend.sale.application.SalePostImageService;
import com.treasureverse.backend.sale.application.SalePostService;
import com.treasureverse.backend.sale.domain.SalePost;
import com.treasureverse.backend.sale.domain.SaleStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sale-posts")
public class SalePostController {
  private final SalePostService salePostService;
  private final SalePostImageService salePostImageService;

  public SalePostController(
      SalePostService salePostService, SalePostImageService salePostImageService) {
    this.salePostService = salePostService;
    this.salePostImageService = salePostImageService;
  }

  record Create(
      @NotBlank @Size(max = 100) String title,
      @NotBlank String content,
      @Positive BigDecimal price) {}

  record Update(
      @NotBlank String title,
      @NotBlank String content,
      @Positive BigDecimal price,
      @NotNull SaleStatus status) {}

  record Image(String objectKey) {}

  record View(Long id, String title, BigDecimal price, SaleStatus status) {}

  private SalePost post(Long id) {
    return salePostService.findById(id);
  }

  private void owner(SalePost p, String id) {
    if (!p.sellerId().equals(Long.valueOf(id))) throw new BusinessException(ErrorCode.CONFLICT);
  }

  @PostMapping
  public ApiResponse<View> create(
      @Valid @RequestBody Create r, @AuthenticationPrincipal String userId) {
    var p = salePostService.create(Long.valueOf(userId), r.title(), r.content(), r.price());
    return ApiResponse.success(new View(p.getId(), p.getTitle(), p.getPrice(), p.getStatus()));
  }

  @PostMapping("/{id}/images")
  public ApiResponse<Void> image(
      @PathVariable Long id, @RequestBody Image r, @AuthenticationPrincipal String uid) {
    salePostImageService.add(id, Long.valueOf(uid), r.objectKey());
    return ApiResponse.success(null);
  }

  @GetMapping
  public ApiResponse<List<View>> list() {
    return ApiResponse.success(
        salePostService.findAll().stream()
            .map(p -> new View(p.getId(), p.getTitle(), p.getPrice(), p.getStatus()))
            .toList());
  }

  @GetMapping("/{id}")
  public ApiResponse<View> get(@PathVariable Long id) {
    var p = post(id);
    return ApiResponse.success(new View(p.getId(), p.getTitle(), p.getPrice(), p.getStatus()));
  }

  @PutMapping("/{id}")
  public ApiResponse<View> update(
      @PathVariable Long id, @Valid @RequestBody Update r, @AuthenticationPrincipal String uid) {
    var p =
        salePostService.update(
            id, Long.valueOf(uid), r.title(), r.content(), r.price(), r.status());
    return ApiResponse.success(new View(p.getId(), p.getTitle(), p.getPrice(), p.getStatus()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal String uid) {
    salePostService.delete(id, Long.valueOf(uid));
    return ResponseEntity.noContent().build();
  }
}
