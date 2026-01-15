package com.example.springskillaryback.service;

import com.example.springskillaryback.common.dto.MeResponse;

public interface UserService {
    MeResponse me(Byte userId);

    void updateUser(Byte userId, String nickname);

    void deleteUser(Byte userId);
}
