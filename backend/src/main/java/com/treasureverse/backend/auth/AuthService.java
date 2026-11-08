package com.treasureverse.backend.auth;

import com.treasureverse.backend.common.exception.BusinessException;
import com.treasureverse.backend.common.exception.ErrorCode;
import com.treasureverse.backend.member.Member;
import com.treasureverse.backend.member.MemberRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {
  private final MemberRepository members;
  private final PasswordEncoder encoder;
  private final JwtTokenProvider jwt;
  private final TokenStore tokens;

  public AuthService(
      MemberRepository members, PasswordEncoder encoder, JwtTokenProvider jwt, TokenStore tokens) {
    this.members = members;
    this.encoder = encoder;
    this.jwt = jwt;
    this.tokens = tokens;
  }

  public boolean available(String username) {
    return !members.existsByUsername(username);
  }

  public void signup(SignupRequest r) {
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
  public AuthToken login(LoginRequest r) {
    Member m =
        members
            .findByUsername(r.username())
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));
    if (!encoder.matches(r.password(), m.getPassword()))
      throw new BusinessException(ErrorCode.INVALID_REQUEST);
    return issue(m);
  }

  public AuthToken refresh(String refreshToken) {
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

  private AuthToken issue(Member m) {
    AuthToken t = jwt.create(m);
    tokens.saveRefresh(
        jwt.parse(t.refreshToken()).getId(),
        m.getId().toString(),
        jwt.remaining(jwt.parse(t.refreshToken())));
    return t;
  }
}
