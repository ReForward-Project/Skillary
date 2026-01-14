package com.example.springskillaryback.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmailVerificationConfirmRequest(
        @NotBlank
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "email must include a full domain"
        )
        String email,
        @NotBlank
        @Pattern(regexp = "\\d{6}", message = "verification code must be 6 digits")
        // 6자리 숫자인지 검증 (문자/기호 불가)
        String code
) {
}
