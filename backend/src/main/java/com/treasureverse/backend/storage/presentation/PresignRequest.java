package com.treasureverse.backend.storage.presentation;

import jakarta.validation.constraints.*;

public record PresignRequest(@NotBlank String contentType, @Positive long size) {}
