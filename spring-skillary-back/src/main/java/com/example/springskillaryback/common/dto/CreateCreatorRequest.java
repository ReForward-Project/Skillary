package com.example.springskillaryback.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCreatorRequest(
        @NotBlank
        @Size(min = 1, max = 500)
        String introduction,

        String profile,        // 선택
        String bankName,       // 선택
        String accountNumber   // 선택
) {}