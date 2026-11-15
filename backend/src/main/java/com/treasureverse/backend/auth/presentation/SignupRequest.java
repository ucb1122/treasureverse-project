package com.treasureverse.backend.auth.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(
    @NotBlank
        @Pattern(
            regexp = "^[a-zA-Z0-9._@-]{4,30}$",
            message = "아이디는 영문, 숫자, 밑줄(_), 점(.), 하이픈(-), 골뱅이(@)만 사용할 수 있습니다.")
        String username,
    @NotBlank @Size(min = 8, max = 72) String password,
    @NotBlank @Size(max = 50) String name,
    @NotBlank @Pattern(regexp = "^[0-9-]{10,20}$") String phoneNumber,
    @Size(max = 10) String postalCode,
    @NotBlank @Size(max = 255) String address,
    @Size(max = 255) String addressDetail) {}
