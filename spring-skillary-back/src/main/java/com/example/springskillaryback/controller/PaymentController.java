package com.example.springskillaryback.controller;

import com.example.springskillaryback.common.dto.BillingOrderRequestDto;
import com.example.springskillaryback.common.dto.CardRequestDto;
import com.example.springskillaryback.common.dto.CardResponseDto;
import com.example.springskillaryback.common.dto.CompleteBillingRequestDto;
import com.example.springskillaryback.common.dto.CompleteBillingResponseDto;
import com.example.springskillaryback.common.dto.CompletePaymentRequestDto;
import com.example.springskillaryback.common.dto.CompletePaymentResponseDto;
import com.example.springskillaryback.common.dto.CustomerKeyResponseDto;
import com.example.springskillaryback.common.dto.OrderResponseDto;
import com.example.springskillaryback.common.dto.PaymentResponseDto;
import com.example.springskillaryback.common.dto.PlanOrderResponseDto;
import com.example.springskillaryback.common.dto.PaymentOrderRequestDto;
import com.example.springskillaryback.common.dto.PaymentOrderResponseDto;
import com.example.springskillaryback.domain.Order;
import com.example.springskillaryback.domain.Payment;
import com.example.springskillaryback.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
	private static final String DEFAULT_USER_EMAIL = "email@email.com";
	private final PaymentService paymentService;

	@PostMapping("/customer-key")
	public ResponseEntity<CustomerKeyResponseDto> getCustomerKey(
			@RequestBody
			String email
	) {
		String customerKey = paymentService.getCustomerKey(email);
		return ResponseEntity.ok(new CustomerKeyResponseDto(customerKey));
	}

	@PostMapping("/cards")
	public ResponseEntity<Void> createCard(
			@Valid
			@RequestBody
			CardRequestDto cardRequestDto
	) {
		var email = cardRequestDto.email();
		var customerKey = cardRequestDto.customerKey();
		var authKey = cardRequestDto.authKey();
		paymentService.createCard(email, customerKey, authKey);
		return ResponseEntity.noContent()
		                     .build();
	}

	@GetMapping("/cards")
	public ResponseEntity<Page<CardResponseDto>> pagingCard(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer size
	) {
		return ResponseEntity.ok(paymentService.pagingCards(DEFAULT_USER_EMAIL, page, size)
		                                       .map(CardResponseDto::from));
	}

	@GetMapping("/orders")
	public ResponseEntity<Page<OrderResponseDto>> pagingOrders(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer size
	) {
		return ResponseEntity.ok(paymentService.pagingOrders(DEFAULT_USER_EMAIL, page, size)
		                                       .map(OrderResponseDto::from));
	}

	@PostMapping("/orders/payment")
	public ResponseEntity<PaymentOrderResponseDto> paymentOrder(
			@Valid
			@RequestBody
			PaymentOrderRequestDto paymentOrderRequestDto
	) {
		var email = paymentOrderRequestDto.email();
		var contentId = paymentOrderRequestDto.contentId();
		var order = paymentService.paymentOrder(email, contentId);
		return ResponseEntity.status(HttpStatus.CREATED)
		                     .body(PaymentOrderResponseDto.from(order));
	}

	@PostMapping("/orders/billing")
	public ResponseEntity<PlanOrderResponseDto> billingOrder(
			@Valid
			@RequestBody
			BillingOrderRequestDto billingOrderRequestDto
	) {
		var email = billingOrderRequestDto.email();
		var planId = billingOrderRequestDto.planId();
		var order = paymentService.billingOrder(email, planId);
		return ResponseEntity.status(HttpStatus.CREATED)
		                     .body(PlanOrderResponseDto.from(order));
	}

	@PostMapping("/complete/payment")
	public ResponseEntity<CompletePaymentResponseDto> completePayment(
			@Valid
			@RequestBody
			CompletePaymentRequestDto completePaymentRequestDto
	) {
		Payment payment = paymentService.completePayment(completePaymentRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED)
		                     .body(CompletePaymentResponseDto.from(payment));
	}

	@PostMapping("/complete/billing")
	public ResponseEntity<CompleteBillingResponseDto> completeBilling(
			@Valid
			@RequestBody
			CompleteBillingRequestDto completeBillingRequestDto
	) {
		Payment payment = paymentService.completeBilling(completeBillingRequestDto);

		return ResponseEntity.status(HttpStatus.CREATED)
		                     .body(CompleteBillingResponseDto.from(payment));
	}

	@GetMapping
	public ResponseEntity<Page<PaymentResponseDto>> pagingPayments(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer size
	) {
		Page<PaymentResponseDto> response = paymentService.pagingPayments(DEFAULT_USER_EMAIL, page, size)
		                                                  .map(PaymentResponseDto::from);
		return ResponseEntity.ok()
		                     .body(response);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<?> retrieveOrder(
			@PathVariable String orderId
	) {
		if (orderId == null)
			throw new IllegalArgumentException("주문 정보 입력 값이 없습니다.");
		Order order = paymentService.retrieveOrder(DEFAULT_USER_EMAIL, orderId);

		if (order.isContent())
			return ResponseEntity.ok(PaymentOrderResponseDto.from(order));

		if (order.isPlan())
			return ResponseEntity.ok(PlanOrderResponseDto.from(order));

		throw new RuntimeException("시스템에 잘못된 값이 저장되어 있습니다.");
	}

	@DeleteMapping("/card/{cardId}")
	public ResponseEntity<Boolean> withdrawCard(
			@PathVariable Byte cardId
	) {
		if (cardId == null)
			throw new IllegalArgumentException("입력값 오류");
		paymentService.withdrawCard(DEFAULT_USER_EMAIL, cardId);
		return ResponseEntity.noContent().build();
	}
}