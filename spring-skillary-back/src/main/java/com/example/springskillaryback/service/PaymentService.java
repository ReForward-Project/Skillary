package com.example.springskillaryback.service;

public interface PaymentService {
	void saveSubscriptionOrder(byte planId);

	void saveContentOrder(byte contentId);

	void completePayment(String paymentKey, String orderId, int credit);

	// activateSubscription 은 SubscriptionService 에서 해야 함 여기 선언하면 안돼요...........
}
