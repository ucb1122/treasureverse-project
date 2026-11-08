package com.treasureverse.backend.auth.application;

import com.treasureverse.backend.auth.domain.TokenProviderPort;
import com.treasureverse.backend.auth.domain.TokenResult;
import com.treasureverse.backend.auth.domain.TokenStorePort;
import com.treasureverse.backend.common.exception.BusinessException;
import com.treasureverse.backend.common.exception.ErrorCode;
import com.treasureverse.backend.member.domain.Member;
import com.treasureverse.backend.member.domain.MemberRepositoryPort;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {
  private final MemberRepositoryPort members;
  private final PasswordEncoder encoder;
  private final TokenProviderPort jwt;
  private final TokenStorePort tokens;

  public AuthService(
      MemberRepositoryPort members,
      PasswordEncoder encoder,
      TokenProviderPort jwt,
      TokenStorePort tokens) {
    this.members = members;
    this.encoder = encoder;
    this.jwt = jwt;
    this.tokens = tokens;
  }

  public boolean available(String username) {
    return !members.existsByUsername(username);
  }

  public void signup(SignupCommand r) {
    if (!available(r.username())) throw new BusinessException(ErrorCode.CONFLICT);
    members.save(
        new Member(
            r.username(),
            encoder.encode(r.password()),
            r.name(),
            r.phoneNumber(),
            r.postalCode(),
            r.address(),
            r.addressDetail()));
  }

  @Transactional(readOnly = true)
  public TokenResult login(LoginCommand r) {
    Member m =
        members
            .findByUsername(r.username())
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));
    if (!encoder.matches(r.password(), m.getPassword()))
      throw new BusinessException(ErrorCode.INVALID_REQUEST);
    return issue(m);
  }

  public TokenResult refresh(String refreshToken) {
    Claims c = jwt.parse(refreshToken);
    if (!"refresh".equals(c.get("type", String.class)) || !tokens.consumeRefresh(c.getId()))
      throw new BusinessException(ErrorCode.INVALID_REQUEST);
    Member m =
        members
            .findById(Long.valueOf(c.getSubject()))
            .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
    return issue(m);
  }

  public void logout(String accessToken) {
    Claims c = jwt.parse(accessToken);
    tokens.blacklistAccess(c.getId(), jwt.remaining(c));
  }

  private TokenResult issue(Member m) {
    TokenResult t = jwt.create(m);
    tokens.saveRefresh(
        jwt.parse(t.refreshToken()).getId(),
        m.getId().toString(),
        jwt.remaining(jwt.parse(t.refreshToken())));
    return t;
  }
}
