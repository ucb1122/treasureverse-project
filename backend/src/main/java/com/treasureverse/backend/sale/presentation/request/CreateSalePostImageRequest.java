package com.treasureverse.backend.sale.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record CreateSalePostImageRequest(@NotBlank String objectKey) {}
