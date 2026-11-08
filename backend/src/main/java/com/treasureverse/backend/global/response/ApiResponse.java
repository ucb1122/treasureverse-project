package com.treasureverse.backend.global.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API success response")
public record ApiResponse<T>(@Schema(example = "true") boolean success, T data) {

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(true, data);
  }
}
