package com.example.springskillaryback.common.dto;

import com.example.springskillaryback.domain.CategoryEnum;

import java.time.LocalDateTime;
import java.util.List;

// 크리에이터 상세 조회 응답 DTO (creator 단건)
public record CreatorDetailResponse(
        Byte creatorId,
        String displayName,
        String introduction,
        CategoryEnum category,
        String profile,
        Byte followCount,
        LocalDateTime createdAt,
        boolean isDeleted,
        List<Byte> planIds
) {}

