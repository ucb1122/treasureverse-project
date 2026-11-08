package com.treasureverse.backend.storage;
import com.treasureverse.backend.common.response.ApiResponse; import jakarta.validation.Valid; import java.util.Map; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/images") public class S3Controller { private final S3Service service; public S3Controller(S3Service s){service=s;} @PostMapping("/presign") public ApiResponse<Map<String,String>> presign(@Valid @RequestBody PresignRequest r){return ApiResponse.success(service.presign(r));} }
