package com.example.springskillaryback.controller;

import com.example.springskillaryback.common.dto.CategoryResponseDto;
import com.example.springskillaryback.common.dto.ContentListResponseDto;
import com.example.springskillaryback.common.dto.ContentRequestDto;
import com.example.springskillaryback.common.dto.ContentResponseDto;
import com.example.springskillaryback.domain.CategoryEnum;
import com.example.springskillaryback.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {
	private final ContentService contentService;

	@PostMapping
	public ResponseEntity<ContentResponseDto> createContent(
		@RequestBody ContentRequestDto requestDto,
		@RequestHeader("X-Creator-Id") Byte creatorId // [임시] 시큐리티 작업전
        // @AuthenticationPrincipal CustomPrincipal customPrincipal
	) {
        // Byte creatorId = customPrincipal.getCreatorId();
		ContentResponseDto response = contentService.createContent(requestDto, creatorId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201
	}

	@PutMapping("/{contentId}")
	public ResponseEntity<ContentResponseDto> updateContent(
		@PathVariable Byte contentId,
		@RequestBody ContentRequestDto requestDto,
		@RequestHeader("X-Creator-Id") Byte creatorId // [임시] 시큐리티 작업전
        // @AuthenticationPrincipal CustomPrincipal customPrincipal
	) {
        // Byte creatorId = customPrincipal.getCreatorId();
		ContentResponseDto response = contentService.updateContent(contentId, requestDto, creatorId);
		return ResponseEntity.ok(response); // 200
	}

	/** 카테고리 목록 조회 */
	@GetMapping("/categories")
	public ResponseEntity<List<CategoryResponseDto>> getCategories() {
		List<CategoryResponseDto> categories = Arrays.stream(CategoryEnum.values())
			.map(CategoryResponseDto::from)
			.toList();
		return ResponseEntity.ok(categories); // 200
	}

	/** 콘텐츠 전체 목록 조회 */
	@GetMapping
	public ResponseEntity<Slice<ContentListResponseDto>> getContents(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Slice<ContentListResponseDto> contents = contentService.getContents(page, size);
		return ResponseEntity.ok(contents); // 200
	}

	/** 인기 콘텐츠 목록 조회 (조회수 기준) */
	@GetMapping("/popular")
	public ResponseEntity<Slice<ContentListResponseDto>> getPopularContents(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Slice<ContentListResponseDto> contents = contentService.getPopularContents(page, size);
		return ResponseEntity.ok(contents); // 200
	}

	/** 크리에이터 기준 목록 조회 */
	@GetMapping("/creators/{creatorId}")
	public ResponseEntity<Slice<ContentListResponseDto>> getContentsByCreator(
		@PathVariable Byte creatorId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Slice<ContentListResponseDto> contents = contentService.getContentsByCreator(creatorId, page, size);
		return ResponseEntity.ok(contents); // 200
	}

	/** 카테고리 기준 목록 조회 */
	@GetMapping("/category/{category}")
	public ResponseEntity<Slice<ContentListResponseDto>> getContentsByCategory(
		@PathVariable CategoryEnum category,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Slice<ContentListResponseDto> contents = contentService.getContentsByCategory(category, page, size);
		return ResponseEntity.ok(contents); // 200
	}

	/** 콘텐츠 상세 조회 (포스트, 댓글 포함) */
	@GetMapping("/{contentId}")
	public ResponseEntity<ContentResponseDto> getContent(@PathVariable Byte contentId) {
		ContentResponseDto content = contentService.getContent(contentId);
		return ResponseEntity.ok(content); // 200
	}

	@DeleteMapping("/{contentId}")
	public ResponseEntity<Void> deleteContent(
		@PathVariable Byte contentId,
		@RequestHeader("X-Creator-Id") Byte creatorId // [임시] 시큐리티 작업전
        // @AuthenticationPrincipal CustomPrincipal customPrincipal
	) {
        // Byte creatorId = customPrincipal.getCreatorId();

		// Content 삭제 (파일 삭제까지 포함하여 처리)
		contentService.deleteContent(contentId, creatorId);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204
	}
}

