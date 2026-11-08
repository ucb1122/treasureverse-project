package com.treasureverse.backend.purchase.presentation;

import com.treasureverse.backend.common.response.ApiResponse;
import com.treasureverse.backend.purchase.application.PurchasePostImageService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/purchase-posts/{postId}/images")
public class PurchasePostImageController {
  private final PurchasePostImageService imageService;

  public PurchasePostImageController(PurchasePostImageService imageService) {
    this.imageService = imageService;
  }

  record Request(@NotBlank String objectKey) {}

  @PostMapping
  public ApiResponse<Void> add(
      @PathVariable Long postId, @RequestBody Request r, @AuthenticationPrincipal String uid) {
    imageService.add(postId, Long.valueOf(uid), r.objectKey());
    return ApiResponse.success(null);
  }
}
