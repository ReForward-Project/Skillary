package com.example.springskillaryback.common.dto;

public record CompleteBillingRequestDto(
		String email,
		String orderId,
		String planName,
		String customerKey,
		int subscriptionFee
) { }