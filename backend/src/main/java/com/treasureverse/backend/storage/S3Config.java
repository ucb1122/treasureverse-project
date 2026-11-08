package com.treasureverse.backend.storage;
import org.springframework.context.annotation.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
@Configuration public class S3Config {
 @Bean S3Presigner s3Presigner(@org.springframework.beans.factory.annotation.Value("${app.storage.region}") String region) { return S3Presigner.builder().region(Region.of(region)).build(); }
}
