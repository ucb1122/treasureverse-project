package com.treasureverse.backend.sale.application;

import com.treasureverse.backend.common.exception.BusinessException;
import com.treasureverse.backend.common.exception.ErrorCode;
import com.treasureverse.backend.sale.domain.SalePostImage;
import com.treasureverse.backend.sale.domain.SalePostImageRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SalePostImageService {
  private final SalePostService salePostService;
  private final SalePostImageRepositoryPort imageRepository;

  public SalePostImageService(
      SalePostService salePostService, SalePostImageRepositoryPort imageRepository) {
    this.salePostService = salePostService;
    this.imageRepository = imageRepository;
  }

  public void add(Long salePostId, Long memberId, String objectKey) {
    var post = salePostService.findById(salePostId);
    if (!post.sellerId().equals(memberId)) throw new BusinessException(ErrorCode.CONFLICT);
    int order = Math.toIntExact(imageRepository.countBySalePostId(salePostId));
    imageRepository.save(new SalePostImage(post, objectKey, order));
  }
}
