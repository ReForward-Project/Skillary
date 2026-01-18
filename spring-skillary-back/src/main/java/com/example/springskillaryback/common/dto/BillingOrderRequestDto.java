package com.example.springskillaryback.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

public record BillingOrderRequestDto(
		@NotEmpty String email,
		@PositiveOrZero byte planId
) { }