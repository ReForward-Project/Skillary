package com.example.springskillaryback.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Table(name = "creator_settlements")
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatorSettlement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Byte creatorSettlementId;

	private int grossAmount;

	private int platformFee;

	private int payoutAmount;

    @Builder.Default
	@Enumerated(STRING)
	private SettlementStatusEnum settlementStatus = SettlementStatusEnum.CALCULATING;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@ManyToOne
	private Creator creator;

	@ManyToOne
	private SettlementRun settlementRun;
}