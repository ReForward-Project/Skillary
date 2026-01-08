package com.example.springskillaryback;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/test")
	public String test() {
		return "Hot Reload Test!";  // 이 문자열 수정
	}
}