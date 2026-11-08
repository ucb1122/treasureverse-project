package com.treasureverse.backend.auth;

import com.treasureverse.backend.common.response.ApiResponse;
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
    service.signup(request);
    return ResponseEntity.status(201).body(ApiResponse.success(null));
  }

  @PostMapping("/login")
  @Operation(summary = "일반 로그인")
  public ApiResponse<AuthToken> login(@Valid @RequestBody LoginRequest request) {
    return ApiResponse.success(service.login(request));
  }

  @PostMapping("/refresh")
  @Operation(summary = "토큰 재발급")
  public ApiResponse<AuthToken> refresh(@Valid @RequestBody RefreshRequest request) {
    return ApiResponse.success(service.refresh(request.refreshToken()));
  }

  @PostMapping("/logout")
  @Operation(summary = "로그아웃")
  public ResponseEntity<ApiResponse<Void>> logout(
      @RequestHeader("Authorization") String authorization) {
    service.logout(authorization.replaceFirst("^Bearer\\s+", ""));
    return ResponseEntity.noContent().build();
  }
}
