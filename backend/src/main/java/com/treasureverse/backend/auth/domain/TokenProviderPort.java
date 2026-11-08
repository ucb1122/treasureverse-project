package com.treasureverse.backend.auth.domain;

import com.treasureverse.backend.member.domain.Member;
import io.jsonwebtoken.Claims;
import java.time.Duration;

public interface TokenProviderPort {
  TokenResult create(Member member);

  Claims parse(String token);

  Duration remaining(Claims claims);
}
