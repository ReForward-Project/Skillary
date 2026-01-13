package com.example.springskillaryback.common.dto;

import com.example.springskillaryback.domain.CategoryEnum;

public record ContentRequestDto(
		String title,
        String description,
		CategoryEnum category,
		Byte planId,
		String thumbnailUrl,
		PostRequestDto post
) { }

