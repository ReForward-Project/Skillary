package com.example.springskillaryback.service.impl;

import com.example.springskillaryback.common.dto.TokensDto;
import com.example.springskillaryback.common.util.JwtTokenizer;
import com.example.springskillaryback.config.JwtProperties;
import com.example.springskillaryback.domain.RefreshToken;
import com.example.springskillaryback.domain.User;
import com.example.springskillaryback.domain.VerifiedEmail;
import com.example.springskillaryback.repository.RefreshTokenRepository;
import com.example.springskillaryback.repository.UserRepository;
import com.example.springskillaryback.repository.VerifiedEmailRepository;
import com.example.springskillaryback.service.AuthService;
import com.example.springskillaryback.service.EmailVerificationService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final VerifiedEmailRepository verifiedEmailRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenizer jwtTokenizer;
    private final JwtProperties jwtProperties;

    @Override
    @Transactional
    public TokensDto register(String email, String password, String nickname) {
        Optional<VerifiedEmail> verifiedEmail = verifiedEmailRepository.findByEmail(email);
        if (verifiedEmail.isEmpty()) {
            throw new IllegalStateException("email not verified");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("email already exists");
        }
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .build();
        User saved = userRepository.save(user);
        
        // verified_emails에서 삭제 (delete + flush)
        verifiedEmailRepository.delete(verifiedEmail.get());
        verifiedEmailRepository.flush();
        
        refreshTokenRepository.deleteByUserId(saved.getUserId());
        String refreshToken = jwtTokenizer.createRefreshToken(saved.getUserId().toString());
        LocalDateTime expiresAt = LocalDateTime.ofInstant(
                Instant.now().plusSeconds(60L * 60 * 24 * jwtProperties.getRefreshExpiryDays()),
                ZoneId.systemDefault()
        );
        refreshTokenRepository.save(new RefreshToken(refreshToken, saved.getUserId(), expiresAt));
        String accessToken = jwtTokenizer.createAccessToken(saved.getUserId().toString());
        return new TokensDto(refreshToken, accessToken);
    }

    @Override
    public TokensDto login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("invalid credentials"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalStateException("invalid credentials");
        }
        refreshTokenRepository.deleteByUserId(user.getUserId());
        String refreshToken = jwtTokenizer.createRefreshToken(user.getUserId().toString());
        LocalDateTime expiresAt = LocalDateTime.ofInstant(
                Instant.now().plusSeconds(60L * 60 * 24 * jwtProperties.getRefreshExpiryDays()),
                ZoneId.systemDefault()
        );
        refreshTokenRepository.save(new RefreshToken(refreshToken, user.getUserId(), expiresAt));
        String accessToken = jwtTokenizer.createAccessToken(user.getUserId().toString());
        return new TokensDto(refreshToken, accessToken);
    }

    @Override
    public void sendCode(String email) {
        emailVerificationService.sendVerificationCode(email);
    }

    @Override
    public boolean verifyCode(String email, String code) {
        // EmailVerificationService에서 이미 verified_emails 저장을 처리함
        return emailVerificationService.verifyCode(email, code);
    }

    @Override
    public void logout(String refreshToken) {
    }

    @Override
    public boolean withdrawal(String refreshToken) {
        return false;
    }

    @Override
    public String refresh(String accessToken) {
        Claims claims = jwtTokenizer.parseClaims(accessToken, true);
        Byte userId = Byte.valueOf(claims.getSubject());
        RefreshToken stored = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("refresh token not found"));
        if (stored.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.deleteByUserId(userId);
            throw new IllegalStateException("refresh token expired");
        }
        return jwtTokenizer.createAccessToken(userId.toString());
    }
}