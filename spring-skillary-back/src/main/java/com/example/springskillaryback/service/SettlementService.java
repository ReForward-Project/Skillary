package com.example.springskillaryback.service;

import com.example.springskillaryback.domain.CreatorSettlement;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface SettlementService {
	void createBatchSettlement(); // 배치 + 스케줄러로 구현해야 할 것?

	void completeBatchSettlement(); // 배치 + 스케줄러로 구현해야 할 것?

	Page<CreatorSettlement> pagingSettlement(byte creatorId, LocalDate startMonth, LocalDate endMonth); // 유저가 쓸 것

	Page<CreatorSettlement> pagingUnsettlement(LocalDate startMonth, LocalDate endMonth);
}