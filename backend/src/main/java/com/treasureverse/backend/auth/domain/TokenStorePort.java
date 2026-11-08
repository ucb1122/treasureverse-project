package com.treasureverse.backend.auth.domain;

import java.time.Duration;

public interface TokenStorePort {
  void saveRefresh(String tokenId, String memberId, Duration ttl);

  boolean consumeRefresh(String tokenId);

  void blacklistAccess(String tokenId, Duration ttl);

  boolean isBlacklisted(String tokenId);
}
