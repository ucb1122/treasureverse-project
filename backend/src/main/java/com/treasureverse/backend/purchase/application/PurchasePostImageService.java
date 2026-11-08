package com.treasureverse.backend.purchase.application;

import com.treasureverse.backend.global.exception.BusinessException;
import com.treasureverse.backend.global.exception.ErrorCode;
import com.treasureverse.backend.purchase.domain.PurchasePostImage;
import com.treasureverse.backend.purchase.domain.PurchasePostImageRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class PurchasePostImageService {
  private final PurchasePostService postService;
  private final PurchasePostImageRepositoryPort imageRepository;

  public PurchasePostImageService(
      PurchasePostService postService, PurchasePostImageRepositoryPort imageRepository) {
    this.postService = postService;
    this.imageRepository = imageRepository;
  }

  public void add(Long postId, Long memberId, String objectKey) {
    var post = postService.findById(postId);
    if (!post.buyerId().equals(memberId)) throw new BusinessException(ErrorCode.CONFLICT);
    imageRepository.save(
        new PurchasePostImage(post, objectKey, (int) imageRepository.countByPostId(postId)));
  }
}
