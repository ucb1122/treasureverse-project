package com.treasureverse.backend.auth.infrastructure;

import com.treasureverse.backend.auth.domain.TokenProviderPort;
import com.treasureverse.backend.auth.domain.TokenStorePort;
import com.treasureverse.backend.global.security.CurrentMember;
import com.treasureverse.backend.member.domain.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final TokenProviderPort jwt;
  private final TokenStorePort tokens;

  public JwtAuthenticationFilter(TokenProviderPort jwt, TokenStorePort tokens) {
    this.jwt = jwt;
    this.tokens = tokens;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      try {
        Claims claims = jwt.parse(header.substring(7));
        if ("access".equals(claims.get("type", String.class))
            && !tokens.isBlacklisted(claims.getId())) {
          var auth =
              new UsernamePasswordAuthenticationToken(
                  new CurrentMember(
                      Long.valueOf(claims.getSubject()),
                      claims.get("username", String.class),
                      Role.valueOf(claims.get("role", String.class))),
                  null,
                  List.of(new SimpleGrantedAuthority("ROLE_" + claims.get("role", String.class))));
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      } catch (RuntimeException ignored) {
        SecurityContextHolder.clearContext();
      }
    }
    chain.doFilter(request, response);
  }
}
