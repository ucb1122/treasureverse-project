package com.treasureverse.backend.storage.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.S3Exception;

/** Local profile에서 MinIO 버킷과 브라우저 직접 업로드 CORS를 준비한다. */
@Slf4j
@Component
@Profile("local")
@RequiredArgsConstructor
public class LocalMinioBucketInitializer {
  private final S3Client s3Client;

  @Value("${app.storage.bucket}")
  private String bucket;

  @EventListener(ApplicationReadyEvent.class)
  public void initialize() {
    createBucketIfMissing();
    log.info("Local MinIO bucket is ready: {}. CORS is configured by Docker Compose.", bucket);
  }

  private void createBucketIfMissing() {
    try {
      s3Client.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
    } catch (NoSuchBucketException exception) {
      s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
    } catch (S3Exception exception) {
      if (exception.statusCode() == 404) {
        s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
        return;
      }
      throw exception;
    }
  }
}
