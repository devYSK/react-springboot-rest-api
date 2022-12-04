package com.ys.librarymanagement.domain.user.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UserCreateResponse {

    private Long userId;

    private String email;

    private String name;

}
