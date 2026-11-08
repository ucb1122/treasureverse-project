package com.treasureverse.backend.sale.application;

import com.treasureverse.backend.common.exception.BusinessException;
import com.treasureverse.backend.common.exception.ErrorCode;
import com.treasureverse.backend.member.domain.MemberRepositoryPort;
import com.treasureverse.backend.sale.domain.SalePost;
import com.treasureverse.backend.sale.domain.SalePostRepositoryPort;
import com.treasureverse.backend.sale.domain.SaleStatus;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SalePostService {
  private final SalePostRepositoryPort postRepository;
  private final MemberRepositoryPort memberRepository;

  public SalePostService(
      SalePostRepositoryPort postRepository, MemberRepositoryPort memberRepository) {
    this.postRepository = postRepository;
    this.memberRepository = memberRepository;
  }

  public SalePost create(Long memberId, String title, String content, BigDecimal price) {
    var member =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
    return postRepository.save(new SalePost(member, title, content, price));
  }

  @Transactional(readOnly = true)
  public SalePost findById(Long id) {
    return postRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public List<SalePost> findAll() {
    return postRepository.findAll();
  }

  public SalePost update(
      Long id, Long memberId, String title, String content, BigDecimal price, SaleStatus status) {
    SalePost post = findById(id);
    verifyOwner(post, memberId);
    post.update(title, content, price, status);
    return post;
  }

  public void delete(Long id, Long memberId) {
    SalePost post = findById(id);
    verifyOwner(post, memberId);
    postRepository.delete(post);
  }

  private void verifyOwner(SalePost post, Long memberId) {
    if (!post.sellerId().equals(memberId)) throw new BusinessException(ErrorCode.CONFLICT);
  }
}
