package com.ys.librarymanagement.domain.user.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 HH시 mm분 ss초", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 HH시 mm분 ss초", timezone = "Asia/Seoul")
    private final LocalDateTime modifiedAt;

    public static UserResponse of(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getCreatedAt(), user.getModifiedAt());
    }

}
