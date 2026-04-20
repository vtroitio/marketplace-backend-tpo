package com.uade.tpo.grupo7.marketplace.users.mapper;

import com.uade.tpo.grupo7.marketplace.users.dto.UserResponse;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getName(),
            user.getSurname(),
            user.getRole().getName(),
            user.isActive()
        );
    }

}
