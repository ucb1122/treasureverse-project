package com.treasureverse.backend.auth.presentation;

import com.treasureverse.backend.auth.application.AuthService;
import com.treasureverse.backend.auth.application.LoginCommand;
import com.treasureverse.backend.auth.application.SignupCommand;
import com.treasureverse.backend.auth.domain.TokenResult;
import com.treasureverse.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthService service;

  public AuthController(AuthService service) {
    this.service = service;
  }

  @GetMapping("/username-availability")
  @Operation(summary = "아이디 사용 가능 여부")
  public ApiResponse<Map<String, Boolean>> available(@RequestParam String username) {
    return ApiResponse.success(Map.of("available", service.available(username)));
  }

  @PostMapping("/signup")
  @Operation(summary = "일반 회원가입")
  public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequest request) {
    service.signup(
        new SignupCommand(
            request.username(),
            request.password(),
            request.name(),
            request.phoneNumber(),
            request.postalCode(),
            request.address(),
            request.addressDetail()));
    return ResponseEntity.status(201).body(ApiResponse.success(null));
  }

  @PostMapping("/login")
  @Operation(summary = "일반 로그인")
  public ApiResponse<AuthToken> login(@Valid @RequestBody LoginRequest request) {
    return ApiResponse.success(
        toAuthToken(service.login(new LoginCommand(request.username(), request.password()))));
  }

  @PostMapping("/refresh")
  @Operation(summary = "토큰 재발급")
  public ApiResponse<AuthToken> refresh(@Valid @RequestBody RefreshRequest request) {
    return ApiResponse.success(toAuthToken(service.refresh(request.refreshToken())));
  }

  @PostMapping("/logout")
  @Operation(summary = "로그아웃")
  public ResponseEntity<ApiResponse<Void>> logout(
      @RequestHeader("Authorization") String authorization) {
    service.logout(authorization.replaceFirst("^Bearer\\s+", ""));
    return ResponseEntity.noContent().build();
  }

  private AuthToken toAuthToken(TokenResult tokenResult) {
    return new AuthToken(
        tokenResult.accessToken(), tokenResult.refreshToken(), tokenResult.accessTokenExpiresIn());
  }
}
