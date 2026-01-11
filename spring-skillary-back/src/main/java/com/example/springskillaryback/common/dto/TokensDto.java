package com.example.springskillaryback.common.dto;

public record TokensDto(
		String refreshToken,
		String accessToken
) { }