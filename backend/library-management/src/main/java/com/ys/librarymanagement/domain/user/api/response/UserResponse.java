package com.ys.librarymanagement.domain.user.api.response;

import com.ys.librarymanagement.domain.user.domain.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponse {

    private final Long userId;

    private final String email;

    private final String name;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public static UserResponse of(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getCreatedAt(), user.getModifiedAt());
    }

}
