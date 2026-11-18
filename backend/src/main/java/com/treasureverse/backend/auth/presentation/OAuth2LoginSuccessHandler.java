package com.treasureverse.backend.auth.presentation;

import com.treasureverse.backend.auth.application.SocialLoginService;
import com.treasureverse.backend.auth.domain.TokenResult;
import com.treasureverse.backend.member.domain.SocialProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Profile("oauth")
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final SocialLoginService socialLoginService;
  private final String frontendRedirectUri;

  public OAuth2LoginSuccessHandler(
      SocialLoginService socialLoginService,
      @Value("${app.oauth.frontend-redirect-uri}") String frontendRedirectUri) {
    this.socialLoginService = socialLoginService;
    this.frontendRedirectUri = frontendRedirectUri;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
    SocialProvider provider =
        SocialProvider.valueOf(token.getAuthorizedClientRegistrationId().toUpperCase());
    SocialUser socialUser = extractUser(provider, token.getPrincipal().getAttributes());

    if (socialLoginService.exists(provider, socialUser.id())) {
      TokenResult result = socialLoginService.loginExisting(provider, socialUser.id());
      getRedirectStrategy().sendRedirect(request, response, tokenRedirectUrl(result));
      return;
    }

    String ticket =
        socialLoginService.createSignupTicket(provider, socialUser.id(), socialUser.name());
    getRedirectStrategy()
        .sendRedirect(request, response, frontendRedirectUri + "?ticket=" + encode(ticket));
  }

  private SocialUser extractUser(SocialProvider provider, Map<String, Object> attributes) {
    if (provider == SocialProvider.NAVER) {
      Object response = attributes.get("response");
      if (!(response instanceof Map<?, ?> profile)) {
        throw new IllegalArgumentException("네이버 사용자 정보를 확인할 수 없습니다.");
      }
      return new SocialUser(
          String.valueOf(profile.get("id")), value(profile.get("name"), "네이버 사용자"));
    }

    return new SocialUser(
        String.valueOf(attributes.get("id")), value(attributes.get("properties"), "카카오 사용자"));
  }

  private String value(Object properties, String fallback) {
    if (properties instanceof Map<?, ?> map && map.get("nickname") != null) {
      return String.valueOf(map.get("nickname"));
    }
    if (properties != null && !(properties instanceof Map<?, ?>)) {
      return String.valueOf(properties);
    }
    return fallback;
  }

  private String tokenRedirectUrl(TokenResult result) {
    return frontendRedirectUri
        + "#accessToken="
        + encode(result.accessToken())
        + "&refreshToken="
        + encode(result.refreshToken());
  }

  private String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

  private record SocialUser(String id, String name) {}
}
