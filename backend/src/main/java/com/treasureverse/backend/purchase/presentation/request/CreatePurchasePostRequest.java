package com.treasureverse.backend.purchase.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreatePurchasePostRequest(
    @NotBlank String title, @NotBlank String content, @Positive BigDecimal desiredPrice) {}
