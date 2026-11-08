package com.treasureverse.backend.purchase.presentation.request;

import com.treasureverse.backend.purchase.domain.PurchaseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record UpdatePurchasePostRequest(
    @NotBlank String title,
    @NotBlank String content,
    @Positive BigDecimal desiredPrice,
    @NotNull PurchaseStatus status) {}
