package com.treasureverse.backend.member.infrastructure;

import com.treasureverse.backend.member.domain.SocialAccount;
import com.treasureverse.backend.member.domain.SocialAccountRepositoryPort;
import com.treasureverse.backend.member.domain.SocialProvider;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SocialAccountRepositoryAdapter implements SocialAccountRepositoryPort {
  private final SocialAccountJpaRepository repository;

  public SocialAccountRepositoryAdapter(SocialAccountJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<SocialAccount> findByProviderAndProviderUserId(
      SocialProvider provider, String providerUserId) {
    return repository.findByProviderAndProviderUserId(provider, providerUserId);
  }

  @Override
  public SocialAccount save(SocialAccount socialAccount) {
    return repository.save(socialAccount);
  }
}
