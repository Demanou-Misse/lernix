package com.lernix.infrastructure.web.mapper;

import com.lernix.domain.model.User;
import com.lernix.infrastructure.web.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.id().value(),
                user.email().value(),
                user.createdAt()
        );
    }
}

