package com.treasureverse.backend.auth.application;

import com.treasureverse.backend.auth.domain.TokenResult;
import com.treasureverse.backend.global.exception.BusinessException;
import com.treasureverse.backend.global.exception.ErrorCode;
import com.treasureverse.backend.member.domain.Member;
import com.treasureverse.backend.member.domain.MemberRepositoryPort;
import com.treasureverse.backend.member.domain.SocialAccount;
import com.treasureverse.backend.member.domain.SocialAccountRepositoryPort;
import com.treasureverse.backend.member.domain.SocialLoginTicket;
import com.treasureverse.backend.member.domain.SocialProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SocialLoginService {
  private final AuthService authService;
  private final MemberRepositoryPort memberRepository;
  private final SocialAccountRepositoryPort socialAccountRepository;
  private final SocialLoginTicketStore ticketStore;

  public SocialLoginService(
      AuthService authService,
      MemberRepositoryPort memberRepository,
      SocialAccountRepositoryPort socialAccountRepository,
      SocialLoginTicketStore ticketStore) {
    this.authService = authService;
    this.memberRepository = memberRepository;
    this.socialAccountRepository = socialAccountRepository;
    this.ticketStore = ticketStore;
  }

  public TokenResult loginExisting(SocialProvider provider, String providerUserId) {
    SocialAccount socialAccount =
        socialAccountRepository
            .findByProviderAndProviderUserId(provider, providerUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
    return authService.issueForMember(socialAccount.member());
  }

  public boolean exists(SocialProvider provider, String providerUserId) {
    return socialAccountRepository
        .findByProviderAndProviderUserId(provider, providerUserId)
        .isPresent();
  }

  public String createSignupTicket(
      SocialProvider provider, String providerUserId, String suggestedName) {
    return ticketStore.create(new SocialLoginTicket(provider, providerUserId, suggestedName));
  }

  public TokenResult signup(SocialSignupCommand command) {
    SocialLoginTicket ticket = ticketStore.consume(command.ticketId());
    if (exists(ticket.provider(), ticket.providerUserId())) {
      throw new BusinessException(ErrorCode.CONFLICT);
    }

    Member member =
        memberRepository.save(
            new Member(
                createUsername(ticket.provider(), ticket.providerUserId()),
                null,
                command.name(),
                command.phoneNumber(),
                command.postalCode(),
                command.address(),
                command.addressDetail()));
    socialAccountRepository.save(
        new SocialAccount(member, ticket.provider(), ticket.providerUserId()));
    return authService.issueForMember(member);
  }

  private String createUsername(SocialProvider provider, String providerUserId) {
    String base =
        (provider.name().toLowerCase() + "_" + providerUserId).replaceAll("[^a-zA-Z0-9_-]", "");
    base = base.substring(0, Math.min(base.length(), 24));
    String candidate = base;
    int suffix = 1;
    while (memberRepository.existsByUsername(candidate)) {
      candidate = base.substring(0, Math.min(base.length(), 24)) + "_" + suffix++;
    }
    return candidate;
  }
}
