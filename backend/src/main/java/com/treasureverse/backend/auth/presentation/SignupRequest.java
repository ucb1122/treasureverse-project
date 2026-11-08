package com.treasureverse.backend.auth.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(
    @NotBlank @Pattern(regexp = "^[a-zA-Z0-9_]{4,30}$") String username,
    @NotBlank @Size(min = 8, max = 72) String password,
    @NotBlank @Size(max = 50) String name,
    @NotBlank @Pattern(regexp = "^[0-9-]{10,20}$") String phoneNumber,
    @Size(max = 10) String postalCode,
    @NotBlank @Size(max = 255) String address,
    @Size(max = 255) String addressDetail) {}
