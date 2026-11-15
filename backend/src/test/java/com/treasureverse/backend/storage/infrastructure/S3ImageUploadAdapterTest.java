package com.treasureverse.backend.storage.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

class S3ImageUploadAdapterTest {

  @Test
  void localMinioConfigurationCreatesALocalPresignedUrl() {
    S3Config config = new S3Config();
    S3Presigner presigner =
        config.s3Presigner(
            "ap-northeast-2", "http://localhost:9000", "minioadmin", "minioadmin", true);
    S3ImageUploadAdapter adapter =
        new S3ImageUploadAdapter(
            presigner,
            config.s3Client(
                "ap-northeast-2", "http://localhost:9000", "minioadmin", "minioadmin", true),
            "treasureverse-local");

    String uploadUrl = adapter.createUploadUrl("images/test-image.png", "image/png");

    assertThat(uploadUrl)
        .startsWith("http://localhost:9000/treasureverse-local/images/test-image.png");
  }
}
