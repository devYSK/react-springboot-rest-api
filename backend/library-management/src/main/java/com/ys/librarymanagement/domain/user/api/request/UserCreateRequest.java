package com.ys.librarymanagement.domain.user.api.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class UserCreateRequest {

    @Email(message = "이메일 형식이 잘못되었습니다.")
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 1, max = 12)
    private String name;

}
