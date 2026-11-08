package com.treasureverse.backend.sale.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CreateSalePostRequest(
    @NotBlank @Size(max = 100) String title,
    @NotBlank String content,
    @Positive BigDecimal price) {}
