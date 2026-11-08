package com.treasureverse.backend.common.health;

import com.treasureverse.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

  @GetMapping
  @Operation(summary = "Application health check")
  public ResponseEntity<ApiResponse<Map<String, String>>> health() {
    return ResponseEntity.ok(ApiResponse.success(Map.of("status", "UP")));
  }
}
