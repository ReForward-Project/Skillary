package com.example.springskillaryback.common.dto;

public record CompletePaymentRequestDto(
		String email,
		String orderId,
		String paymentKey,
		int amount
) { }
