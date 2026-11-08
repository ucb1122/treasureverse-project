package com.treasureverse.backend.member.infrastructure;

import com.treasureverse.backend.member.domain.SocialAccount;
import com.treasureverse.backend.member.domain.SocialProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialAccountJpaRepository extends JpaRepository<SocialAccount, Long> {
  Optional<SocialAccount> findByProviderAndProviderUserId(
      SocialProvider provider, String providerUserId);
}
