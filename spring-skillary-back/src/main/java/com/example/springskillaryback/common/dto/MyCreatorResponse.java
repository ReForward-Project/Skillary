package com.example.springskillaryback.common.dto;

public record MyCreatorResponse(
        Byte creatorId,
        String displayName,
        String introduction,
        String profile,
        String bankName,
        String accountNumber,
        Byte followCount,
        boolean isDeleted
) {}