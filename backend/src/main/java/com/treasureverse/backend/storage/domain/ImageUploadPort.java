package com.treasureverse.backend.storage.domain;

public interface ImageUploadPort {
  String createUploadUrl(String objectKey, String contentType);
}
