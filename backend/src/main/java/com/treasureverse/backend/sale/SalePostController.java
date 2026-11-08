package com.treasureverse.backend.sale;

import com.treasureverse.backend.common.exception.*;
import com.treasureverse.backend.common.response.ApiResponse;
import com.treasureverse.backend.member.MemberRepository;
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
  private final SalePostRepository posts;
  private final MemberRepository members;
  private final SalePostImageRepository images;

  public SalePostController(SalePostRepository p, MemberRepository m, SalePostImageRepository i) {
    posts = p;
    members = m;
    images = i;
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
    return posts
        .findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
  }

  private void owner(SalePost p, String id) {
    if (!p.sellerId().equals(Long.valueOf(id))) throw new BusinessException(ErrorCode.CONFLICT);
  }

  @PostMapping
  public ApiResponse<View> create(
      @Valid @RequestBody Create r, @AuthenticationPrincipal String userId) {
    var m =
        members
            .findById(Long.valueOf(userId))
            .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
    var p = posts.save(new SalePost(m, r.title(), r.content(), r.price()));
    return ApiResponse.success(new View(p.getId(), p.getTitle(), p.getPrice(), p.getStatus()));
  }

  @PostMapping("/{id}/images")
  public ApiResponse<Void> image(
      @PathVariable Long id, @RequestBody Image r, @AuthenticationPrincipal String uid) {
    var p = post(id);
    owner(p, uid);
    images.save(
        new SalePostImage(p, r.objectKey(), images.findBySalePostIdOrderBySortOrder(id).size()));
    return ApiResponse.success(null);
  }

  @GetMapping
  public ApiResponse<List<View>> list() {
    return ApiResponse.success(
        posts.findAll().stream()
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
    var p = post(id);
    owner(p, uid);
    p.update(r.title(), r.content(), r.price(), r.status());
    return ApiResponse.success(new View(p.getId(), p.getTitle(), p.getPrice(), p.getStatus()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal String uid) {
    var p = post(id);
    owner(p, uid);
    posts.delete(p);
    return ResponseEntity.noContent().build();
  }
}
