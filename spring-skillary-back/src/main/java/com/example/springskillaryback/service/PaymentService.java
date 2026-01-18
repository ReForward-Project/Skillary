package com.example.springskillaryback.service;

import com.example.springskillaryback.common.dto.CompleteBillingRequestDto;
import com.example.springskillaryback.common.dto.CompletePaymentRequestDto;
import com.example.springskillaryback.domain.Card;
import com.example.springskillaryback.domain.Order;
import com.example.springskillaryback.domain.Payment;
import org.springframework.data.domain.Page;

public interface PaymentService {
	String getCustomerKey(String email);

	/* Card */
	Card createCard(String email, String customerKey, String authKey);

	Page<Card> pagingCards(String email, int page, int size);

	void withdrawCard(String email, byte cardId);

	/* Order */
	Order paymentOrder(String email, byte contentId);

	Order billingOrder(String email, byte planId);

	Page<Order> pagingOrders(String email, int page, int size);

	Order retrieveOrder(String email, String orderId);

	/* Payment */
	Page<Payment> pagingPayments(String email, int page, int size);

	Payment completeBilling(CompleteBillingRequestDto completeBillingRequestDto);

	Payment completePayment(CompletePaymentRequestDto completePaymentRequestDto);
}
