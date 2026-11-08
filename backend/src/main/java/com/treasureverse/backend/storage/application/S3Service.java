package com.treasureverse.backend.storage.application;

import com.treasureverse.backend.global.exception.*;
import com.treasureverse.backend.storage.domain.ImageUploadPort;
import com.treasureverse.backend.storage.presentation.PresignRequest;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class S3Service {
  private final ImageUploadPort imageUploadPort;
  private final long max;

  public S3Service(
      ImageUploadPort imageUploadPort, @Value("${app.storage.max-image-bytes}") long m) {
    this.imageUploadPort = imageUploadPort;
    max = m;
  }

  public Map<String, String> presign(PresignRequest r) {
    if (!r.contentType().startsWith("image/") || r.size() > max)
      throw new BusinessException(ErrorCode.INVALID_REQUEST);
    String key = "images/" + UUID.randomUUID();
    var url = imageUploadPort.createUploadUrl(key, r.contentType());
    return Map.of("key", key, "uploadUrl", url);
  }
}
