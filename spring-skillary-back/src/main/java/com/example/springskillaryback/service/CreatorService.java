package com.example.springskillaryback.service;

import com.example.springskillaryback.common.dto.CreateCreatorRequest;

public interface CreatorService {
    Byte createCreator(Byte userId, CreateCreatorRequest request);

    
}
