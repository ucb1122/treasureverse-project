package com.treasureverse.backend.auth.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SocialSignupRequest(
    @NotBlank String ticketId,
    @NotBlank @Size(max = 50) String name,
    @NotBlank @Pattern(regexp = "^[0-9-]{10,20}$") String phoneNumber,
    @Size(max = 10) String postalCode,
    @NotBlank @Size(max = 255) String address,
    @Size(max = 255) String addressDetail) {}
