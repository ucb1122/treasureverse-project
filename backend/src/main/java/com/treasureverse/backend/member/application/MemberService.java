package com.treasureverse.backend.member.application;

import com.treasureverse.backend.common.exception.BusinessException;
import com.treasureverse.backend.common.exception.ErrorCode;
import com.treasureverse.backend.member.domain.Member;
import com.treasureverse.backend.member.domain.MemberRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {
  private final MemberRepositoryPort memberRepository;

  public MemberService(MemberRepositoryPort memberRepository) {
    this.memberRepository = memberRepository;
  }

  public Member getMyProfile(String username) {
    return memberRepository
        .findByUsername(username)
        .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
  }

  @Transactional
  public Member updateMyProfile(
      String username,
      String name,
      String phoneNumber,
      String postalCode,
      String address,
      String addressDetail) {
    Member member = getMyProfile(username);
    member.updateProfile(name, phoneNumber, postalCode, address, addressDetail);
    return member;
  }
}
