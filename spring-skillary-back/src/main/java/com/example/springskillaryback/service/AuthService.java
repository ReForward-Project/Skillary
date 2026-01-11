package com.example.springskillaryback.service;

import com.example.springskillaryback.common.dto.TokensDto;

public interface AuthService {
	void register(String email, String password);

	TokensDto login(String email, String password);

	void sendCode(String email);

	boolean verifyCode(String email, String code);

	void logout(String refreshToken);

	boolean withdrawal(String refreshToken);

	String refresh(String refreshToken);
}
