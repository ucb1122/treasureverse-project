package com.treasureverse.backend.auth.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@Profile("oauth")
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
  private final String frontendRedirectUri;

  public OAuth2LoginFailureHandler(
      @Value("${app.oauth.frontend-redirect-uri}") String frontendRedirectUri) {
    this.frontendRedirectUri = frontendRedirectUri;
  }

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    getRedirectStrategy()
        .sendRedirect(request, response, frontendRedirectUri + "?error=oauth_login_failed");
  }
}
