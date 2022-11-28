package com.ys.librarymanagement.user.domain;

import com.ys.librarymanagement.common.base.AbstractTimeColumn;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AbstractTimeColumn {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public static User create(String email, String name) {
        validateEmail(email);
        validateUserName(name);

        return new User(email, name);
    }

    private static void validateEmail(String email) {

        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("이메일은 비어있어선 안됩니다." + email);
        }

        Pattern emailPattern = Pattern.compile(
            "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");

        if (!emailPattern.matcher(email).matches()) {
            throw new IllegalArgumentException("이메일 형식이 잘못되었습니다.");
        }
    }

    private static void validateUserName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("이름은 비어이었어선 안됩니다. " + name);
        }

        if (name.length() > 12) {
            throw new IllegalArgumentException("이름은 12자 이하여야 합니다. " + name);
        }
    }

}
