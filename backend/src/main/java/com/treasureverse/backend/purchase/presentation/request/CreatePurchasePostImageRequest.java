package com.treasureverse.backend.purchase.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record CreatePurchasePostImageRequest(@NotBlank String objectKey) {}
