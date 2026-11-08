package com.treasureverse.backend.purchase.presentation;

import com.treasureverse.backend.global.response.ApiResponse;
import com.treasureverse.backend.global.security.CurrentMember;
import com.treasureverse.backend.purchase.application.PurchasePostService;
import com.treasureverse.backend.purchase.presentation.request.CreatePurchasePostRequest;
import com.treasureverse.backend.purchase.presentation.request.UpdatePurchasePostRequest;
import com.treasureverse.backend.purchase.presentation.response.PurchasePostResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/purchase-posts")
public class PurchasePostController {
  private final PurchasePostService purchasePostService;

  public PurchasePostController(PurchasePostService purchasePostService) {
    this.purchasePostService = purchasePostService;
  }

  @PostMapping
  public ApiResponse<PurchasePostResponse> create(
      @Valid @RequestBody CreatePurchasePostRequest request,
      @AuthenticationPrincipal CurrentMember currentMember) {
    return ApiResponse.success(
        PurchasePostResponse.from(
            purchasePostService.create(
                currentMember.id(), request.title(), request.content(), request.desiredPrice())));
  }

  @GetMapping
  public ApiResponse<List<PurchasePostResponse>> list() {
    return ApiResponse.success(
        purchasePostService.findAll().stream().map(PurchasePostResponse::from).toList());
  }

  @GetMapping("/{id}")
  public ApiResponse<PurchasePostResponse> get(@PathVariable Long id) {
    return ApiResponse.success(PurchasePostResponse.from(purchasePostService.findById(id)));
  }

  @PutMapping("/{id}")
  public ApiResponse<PurchasePostResponse> update(
      @PathVariable Long id,
      @Valid @RequestBody UpdatePurchasePostRequest request,
      @AuthenticationPrincipal CurrentMember currentMember) {
    return ApiResponse.success(
        PurchasePostResponse.from(
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
}
