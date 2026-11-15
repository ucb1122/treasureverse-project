package com.treasureverse.backend.storage.domain;

public interface ImageUploadPort {
  String createUploadUrl(String objectKey, String contentType);

  String createViewUrl(String objectKey);

  void upload(String objectKey, String contentType, byte[] content);
}
