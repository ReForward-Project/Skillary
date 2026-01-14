package com.example.springskillaryback.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "이메일 주소를 끝까지 입력해주세요 (예: example@gmail.com)"
        )
        String email,
        @NotBlank
        @Pattern(
                regexp = "^[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};:'\",.<>/?`~|]+$",
                message = "비밀번호는 영문, 숫자, 특수문자만 사용할 수 있습니다"
        )
        @Size(min = 8, max = 72)
        String password,
        @NotBlank
        @Pattern(
                regexp = "^[^\\s].*[^\\s]$|^[^\\s]+$",
                message = "닉네임 앞뒤에 공백을 사용할 수 없습니다"
        )
        @Size(max = 100)
        String nickname
) {
}
