package com.example.springskillaryback.domain;

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

@Table(name = "subscribes")
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Subscribe {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Byte subscribeId;

    @Builder.Default
	@Enumerated(STRING)
	private SubscribeStatusEnum subscribeStatus = SubscribeStatusEnum.INACTIVE;

	@CreationTimestamp
	private LocalDateTime createdAt;

    @Builder.Default
	private LocalDateTime startAt = null;

    @Builder.Default
	private LocalDateTime endAt = null;

	@ManyToOne
	@JoinColumn(name = "plan_id", nullable = false)
	private SubscriptionPlan subscriptionPlan;
}