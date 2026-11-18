package com.treasureverse.backend.purchase.presentation;

import com.treasureverse.backend.global.response.ApiResponse;
import com.treasureverse.backend.global.response.PageResponse;
import com.treasureverse.backend.global.security.CurrentMember;
import com.treasureverse.backend.purchase.application.PurchasePostImageService;
import com.treasureverse.backend.purchase.application.PurchasePostService;
import com.treasureverse.backend.purchase.presentation.request.CreatePurchasePostRequest;
import com.treasureverse.backend.purchase.presentation.request.UpdatePurchasePostRequest;
import com.treasureverse.backend.purchase.presentation.response.PurchasePostResponse;
import com.treasureverse.backend.storage.application.S3Service;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/purchase-posts")
public class PurchasePostController {
  private final PurchasePostService purchasePostService;
  private final PurchasePostImageService purchasePostImageService;
  private final S3Service storageService;

  public PurchasePostController(
      PurchasePostService purchasePostService,
      PurchasePostImageService purchasePostImageService,
      S3Service storageService) {
    this.purchasePostService = purchasePostService;
    this.purchasePostImageService = purchasePostImageService;
    this.storageService = storageService;
  }

  @PostMapping
  public ApiResponse<PurchasePostResponse> create(
      @Valid @RequestBody CreatePurchasePostRequest request,
      @AuthenticationPrincipal CurrentMember currentMember) {
    return ApiResponse.success(
        toResponse(
            purchasePostService.create(
                currentMember.id(), request.title(), request.content(), request.desiredPrice())));
  }

  @GetMapping
  public ApiResponse<PageResponse<PurchasePostResponse>> list(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "latest") String sort) {
    int validatedPage = Math.max(page, 0);
    int validatedSize = Math.min(Math.max(size, 1), 50);
    return ApiResponse.success(
        PageResponse.from(
            purchasePostService
                .findPage(PageRequest.of(validatedPage, validatedSize, toSort(sort)))
                .map(this::toResponse)));
  }

  @GetMapping("/{id}")
  public ApiResponse<PurchasePostResponse> get(@PathVariable Long id) {
    return ApiResponse.success(toResponse(purchasePostService.findById(id)));
  }

  @PutMapping("/{id}")
  public ApiResponse<PurchasePostResponse> update(
      @PathVariable Long id,
      @Valid @RequestBody UpdatePurchasePostRequest request,
      @AuthenticationPrincipal CurrentMember currentMember) {
    return ApiResponse.success(
        toResponse(
            purchasePostService.update(
                id,
                currentMember.id(),
                request.title(),
                request.content(),
                request.desiredPrice(),
                request.status())));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @PathVariable Long id, @AuthenticationPrincipal CurrentMember currentMember) {
    purchasePostService.delete(id, currentMember.id());
    return ResponseEntity.noContent().build();
  }

  private Sort toSort(String sort) {
    return switch (sort) {
      case "priceAsc" -> Sort.by("desiredPrice").ascending().and(Sort.by("id").descending());
      case "priceDesc" -> Sort.by("desiredPrice").descending().and(Sort.by("id").descending());
      default -> Sort.by("id").descending();
    };
  }

  private PurchasePostResponse toResponse(
      com.treasureverse.backend.purchase.domain.PurchasePost purchasePost) {
    return PurchasePostResponse.from(
        purchasePost,
        purchasePostImageService.findObjectKeys(purchasePost.id()).stream()
            .map(storageService::viewUrl)
            .toList());
  }
}
