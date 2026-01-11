package com.example.springskillaryback.common.dto;

public record CommentRequestDto(
		byte userId,
		byte postId,
		String comment
) { }