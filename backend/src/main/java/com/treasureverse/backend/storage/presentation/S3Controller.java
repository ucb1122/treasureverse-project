package com.treasureverse.backend.storage.presentation;

import com.treasureverse.backend.global.response.ApiResponse;
import com.treasureverse.backend.storage.application.S3Service;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
public class S3Controller {
  private final S3Service service;

  public S3Controller(S3Service s) {
    service = s;
  }

  @PostMapping("/presign")
  public ApiResponse<Map<String, String>> presign(@Valid @RequestBody PresignRequest r) {
    return ApiResponse.success(service.presign(r));
  }

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResponse<Map<String, String>> upload(@RequestPart("file") MultipartFile file) {
    return ApiResponse.success(service.upload(file));
  }
}
