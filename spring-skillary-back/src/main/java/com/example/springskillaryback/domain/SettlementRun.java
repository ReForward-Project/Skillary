package com.example.springskillaryback.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "settlement_runs")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SettlementRun {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Byte runId;

    @Builder.Default
	@Enumerated(STRING)
	private RunStatusEnum runStatus = RunStatusEnum.READY;

	@CreationTimestamp
	private LocalDate periodStart;

    @Builder.Default
	private LocalDate periodEnd = null;

    @Builder.Default
	private LocalDateTime executedAt = null;

    @Builder.Default
	@OneToMany
	@JoinColumn(name = "run_id")
	private Set<CreatorSettlement> settlements = new HashSet<>();
}