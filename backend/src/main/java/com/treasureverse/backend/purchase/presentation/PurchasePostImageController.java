package com.treasureverse.backend.purchase.presentation;

import com.treasureverse.backend.common.response.ApiResponse;
import com.treasureverse.backend.common.security.CurrentMember;
import com.treasureverse.backend.purchase.application.PurchasePostImageService;
import com.treasureverse.backend.purchase.presentation.request.CreatePurchasePostImageRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/purchase-posts/{postId}/images")
public class PurchasePostImageController {
  private final PurchasePostImageService purchasePostImageService;

  public PurchasePostImageController(PurchasePostImageService purchasePostImageService) {
    this.purchasePostImageService = purchasePostImageService;
  }

  @PostMapping
  public ApiResponse<Void> add(
      @PathVariable Long postId,
      @Valid @RequestBody CreatePurchasePostImageRequest request,
      @AuthenticationPrincipal CurrentMember currentMember) {
    purchasePostImageService.add(postId, currentMember.id(), request.objectKey());
    return ApiResponse.success(null);
  }
}
