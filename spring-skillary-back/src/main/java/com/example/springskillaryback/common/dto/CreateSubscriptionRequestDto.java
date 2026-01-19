package com.example.springskillaryback.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record CreateSubscriptionRequestDto(
		@NotEmpty String planName,
		@NotEmpty String description,
		@Positive int price
) { }