package com.example.springskillaryback.service;

import com.example.springskillaryback.domain.Subscribe;

import java.util.List;

public interface SubscrptionService {
	Subscribe subscribe(byte userId, byte creatorId);

	void deleteSubscribe(byte userId, byte creatorId);

	List<Subscribe> pagingSubscribes(byte userId);
}
