package com.treasureverse.backend.purchase.application;

import com.treasureverse.backend.common.exception.BusinessException;
import com.treasureverse.backend.common.exception.ErrorCode;
import com.treasureverse.backend.member.domain.MemberRepositoryPort;
import com.treasureverse.backend.purchase.domain.PurchasePost;
import com.treasureverse.backend.purchase.domain.PurchasePostRepositoryPort;
import com.treasureverse.backend.purchase.domain.PurchaseStatus;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PurchasePostService {
  private final PurchasePostRepositoryPort postRepository;
  private final MemberRepositoryPort memberRepository;

  public PurchasePostService(
      PurchasePostRepositoryPort postRepository, MemberRepositoryPort memberRepository) {
    this.postRepository = postRepository;
    this.memberRepository = memberRepository;
  }

  public PurchasePost create(Long memberId, String title, String content, BigDecimal price) {
    var member =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
    return postRepository.save(new PurchasePost(member, title, content, price));
  }

  @Transactional(readOnly = true)
  public List<PurchasePost> findAll() {
    return postRepository.findAll();
  }

  @Transactional(readOnly = true)
  public PurchasePost findById(Long id) {
    return postRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
  }

  public PurchasePost update(
      Long id,
      Long memberId,
      String title,
      String content,
      BigDecimal price,
      PurchaseStatus status) {
    PurchasePost post = findById(id);
    verifyOwner(post, memberId);
    post.update(title, content, price, status);
    return post;
  }

  public void delete(Long id, Long memberId) {
    PurchasePost post = findById(id);
    verifyOwner(post, memberId);
    postRepository.delete(post);
  }

  private void verifyOwner(PurchasePost post, Long memberId) {
    if (!post.buyerId().equals(memberId)) throw new BusinessException(ErrorCode.CONFLICT);
  }
}
