package com.treasureverse.backend.storage.infrastructure;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.checksums.RequestChecksumCalculation;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {
  @Bean
  S3Client s3Client(
      @Value("${app.storage.region}") String region,
      @Value("${app.storage.endpoint:}") String endpoint,
      @Value("${app.storage.access-key:}") String accessKey,
      @Value("${app.storage.secret-key:}") String secretKey,
      @Value("${app.storage.path-style-access:false}") boolean pathStyleAccess) {
    S3ClientBuilder builder = S3Client.builder().region(Region.of(region));
    configureEndpoint(builder, endpoint, accessKey, secretKey, pathStyleAccess);
    return builder.build();
  }

  @Bean
  S3Presigner s3Presigner(S3Client s3Client, @Value("${app.storage.region}") String region) {
    return S3Presigner.builder().s3Client(s3Client).region(Region.of(region)).build();
  }

  private void configureEndpoint(
      S3ClientBuilder builder,
      String endpoint,
      String accessKey,
      String secretKey,
      boolean pathStyleAccess) {
    if (!endpoint.isBlank()) {
      builder
          .endpointOverride(URI.create(endpoint))
          .requestChecksumCalculation(RequestChecksumCalculation.WHEN_REQUIRED)
          .credentialsProvider(
              StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
          .serviceConfiguration(
              S3Configuration.builder().pathStyleAccessEnabled(pathStyleAccess).build());
    }
  }
}
