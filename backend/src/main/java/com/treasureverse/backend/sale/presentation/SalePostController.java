package com.treasureverse.backend.sale.presentation;

import com.treasureverse.backend.common.response.ApiResponse;
import com.treasureverse.backend.common.security.CurrentMember;
import com.treasureverse.backend.sale.application.SalePostImageService;
import com.treasureverse.backend.sale.application.SalePostService;
import com.treasureverse.backend.sale.presentation.request.CreateSalePostImageRequest;
import com.treasureverse.backend.sale.presentation.request.CreateSalePostRequest;
import com.treasureverse.backend.sale.presentation.request.UpdateSalePostRequest;
import com.treasureverse.backend.sale.presentation.response.SalePostResponse;
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
@RequestMapping("/api/v1/sale-posts")
public class SalePostController {
  private final SalePostService salePostService;
  private final SalePostImageService salePostImageService;

  public SalePostController(
      SalePostService salePostService, SalePostImageService salePostImageService) {
    this.salePostService = salePostService;
    this.salePostImageService = salePostImageService;
  }

  @PostMapping
  public ApiResponse<SalePostResponse> create(
      @Valid @RequestBody CreateSalePostRequest request,
      @AuthenticationPrincipal CurrentMember currentMember) {
    return ApiResponse.success(
        SalePostResponse.from(
            salePostService.create(
                currentMember.id(), request.title(), request.content(), request.price())));
  }

  @PostMapping("/{id}/images")
  public ApiResponse<Void> addImage(
      @PathVariable Long id,
      @Valid @RequestBody CreateSalePostImageRequest request,
      @AuthenticationPrincipal CurrentMember currentMember) {
    salePostImageService.add(id, currentMember.id(), request.objectKey());
    return ApiResponse.success(null);
  }

  @GetMapping
  public ApiResponse<List<SalePostResponse>> list() {
    return ApiResponse.success(
        salePostService.findAll().stream().map(SalePostResponse::from).toList());
  }

  @GetMapping("/{id}")
  public ApiResponse<SalePostResponse> get(@PathVariable Long id) {
    return ApiResponse.success(SalePostResponse.from(salePostService.findById(id)));
  }

  @PutMapping("/{id}")
  public ApiResponse<SalePostResponse> update(
      @PathVariable Long id,
      @Valid @RequestBody UpdateSalePostRequest request,
      @AuthenticationPrincipal CurrentMember currentMember) {
    return ApiResponse.success(
        SalePostResponse.from(
            salePostService.update(
                id,
                currentMember.id(),
                request.title(),
                request.content(),
                request.price(),
                request.status())));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @PathVariable Long id, @AuthenticationPrincipal CurrentMember currentMember) {
    salePostService.delete(id, currentMember.id());
    return ResponseEntity.noContent().build();
  }
}
