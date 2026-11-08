package com.treasureverse.backend.common.exception;

import java.util.Map;

public record ErrorResponse(
    boolean success, String code, String message, Map<String, String> fieldErrors) {

  public static ErrorResponse of(ErrorCode errorCode) {
    return new ErrorResponse(false, errorCode.getCode(), errorCode.getMessage(), Map.of());
  }

  public static ErrorResponse of(ErrorCode errorCode, Map<String, String> fieldErrors) {
    return new ErrorResponse(false, errorCode.getCode(), errorCode.getMessage(), fieldErrors);
  }
}
