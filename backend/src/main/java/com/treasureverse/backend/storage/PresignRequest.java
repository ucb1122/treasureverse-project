package com.treasureverse.backend.storage;

import jakarta.validation.constraints.*;

public record PresignRequest(@NotBlank String contentType, @Positive long size) {}
