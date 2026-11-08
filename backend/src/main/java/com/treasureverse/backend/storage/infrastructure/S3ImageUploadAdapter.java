package com.treasureverse.backend.storage.infrastructure;

import com.treasureverse.backend.storage.domain.ImageUploadPort;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
public class S3ImageUploadAdapter implements ImageUploadPort {
  private final S3Presigner presigner;
  private final String bucket;

  public S3ImageUploadAdapter(
      S3Presigner presigner, @Value("${app.storage.bucket}") String bucket) {
    this.presigner = presigner;
    this.bucket = bucket;
  }

  public String createUploadUrl(String key, String contentType) {
    var request =
        PutObjectRequest.builder().bucket(bucket).key(key).contentType(contentType).build();
    return presigner
        .presignPutObject(
            PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(request)
                .build())
        .url()
        .toString();
  }
}
