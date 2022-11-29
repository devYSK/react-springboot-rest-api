package com.ys.librarymanagement.domain.user.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class UserCreateResponse {

    private final Long userId;

    private final String email;

    private final String name;

}