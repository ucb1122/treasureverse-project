package com.treasureverse.backend.storage;
import com.treasureverse.backend.common.exception.*;
import java.time.Duration; import java.util.*;
import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest; import software.amazon.awssdk.services.s3.presigner.S3Presigner; import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
@Service public class S3Service {
 private final S3Presigner presigner; private final String bucket; private final long max;
 public S3Service(S3Presigner p,@Value("${app.storage.bucket}") String b,@Value("${app.storage.max-image-bytes}") long m){presigner=p;bucket=b;max=m;}
 public Map<String,String> presign(PresignRequest r){ if(bucket.isBlank()||!r.contentType().startsWith("image/")||r.size()>max) throw new BusinessException(ErrorCode.INVALID_REQUEST); String key="images/"+UUID.randomUUID(); var put=PutObjectRequest.builder().bucket(bucket).key(key).contentType(r.contentType()).build(); var url=presigner.presignPutObject(PutObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(10)).putObjectRequest(put).build()).url().toString(); return Map.of("key",key,"uploadUrl",url); }
}
