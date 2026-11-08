package com.treasureverse.backend.sale.presentation.request;

import com.treasureverse.backend.sale.domain.SaleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record UpdateSalePostRequest(
    @NotBlank String title,
    @NotBlank String content,
    @Positive BigDecimal price,
    @NotNull SaleStatus status) {}
