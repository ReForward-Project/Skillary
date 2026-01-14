package com.example.springskillaryback.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmailVerificationRequest(
        @NotBlank
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "이메일 주소를 끝까지 입력해주세요 (예: example@gmail.com)"
        )
        String email
) {
}