package com.treasureverse.backend.member.domain;

import java.util.Optional;

public interface SocialAccountRepositoryPort {
  Optional<SocialAccount> findByProviderAndProviderUserId(
      SocialProvider provider, String providerUserId);

  SocialAccount save(SocialAccount socialAccount);
}
