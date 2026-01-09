package com.example.springskillaryback.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "subscription_plans")
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPlan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Byte planId;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false)
	private int price;

    @Builder.Default
	private boolean isActive = false;

	@CreationTimestamp
	private LocalDateTime createdAt;
}