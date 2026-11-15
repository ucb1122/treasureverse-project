package com.treasureverse.backend.storage.infrastructure;

import com.treasureverse.backend.storage.domain.ImageUploadPort;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
public class S3ImageUploadAdapter implements ImageUploadPort {
  private final S3Presigner presigner;
  private final S3Client s3Client;
  private final String bucket;

  public S3ImageUploadAdapter(
      S3Presigner presigner, S3Client s3Client, @Value("${app.storage.bucket}") String bucket) {
    this.presigner = presigner;
    this.s3Client = s3Client;
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

  @Override
  public String createViewUrl(String objectKey) {
    return presigner
        .presignGetObject(
            GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(GetObjectRequest.builder().bucket(bucket).key(objectKey).build())
                .build())
        .url()
        .toString();
  }

  @Override
  public void upload(String objectKey, String contentType, byte[] content) {
    s3Client.putObject(
        PutObjectRequest.builder().bucket(bucket).key(objectKey).contentType(contentType).build(),
        RequestBody.fromBytes(content));
  }
}
