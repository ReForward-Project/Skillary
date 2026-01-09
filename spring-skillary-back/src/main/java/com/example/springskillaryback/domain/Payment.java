package com.example.springskillaryback.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Table(name = "payments")
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Byte paymentId;

	@Column(nullable = false, unique = true)
	private String orderId; // PG 사 orderId(String 의 난수 형식)

	@Column(nullable = false)
	private int amount;

    @Builder.Default
	@Enumerated(STRING)
	private CreditMethodEnum creditMethod = CreditMethodEnum.CARD;

    @Builder.Default
	@Enumerated(STRING)
	private CreditStatusEnum creditStatus = CreditStatusEnum.READY;

    @Builder.Default
	private LocalDateTime paidAt = null;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
}