package com.ys.librarymanagement.user.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.ys.librarymanagement.domain.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserTest {

    @DisplayName("create 테스트 - 입력값이 올바르면 유저 생성에 성공한다.")
    @Test
    void createSuccessTest() {
        //given
        String email = "email@naver.com";
        String name = "name";

        //when
        User user = assertDoesNotThrow(() -> User.create(email, name));

        //then
        assertNotNull(user);

        assertEquals(email, user.getEmail());
        assertEquals(name, user.getName());
    }

    @DisplayName("create 테스트 - 유저 이름이 1자 미만 12자 초과이면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {" ", "", "1234567890101", "가나다라마바사아자차카타파", "에이비123씨234디50"})
    void createFailNameNotValidTest(String notValidName) {
        //given
        String email = "useremail!@naver.com";

        //when & then
        assertThrows(IllegalArgumentException.class, () -> User.create(email, notValidName));
    }

    @DisplayName("create - validateEmail 테스트 - 이메일이 올바르면 예외를 던지지 않는다 ")
    @ParameterizedTest
    @ValueSource(strings = {"e2@naver.com", "sexxy@google.com", "hot@naver.com",
        "kakaonaver@kakao.com"})
    void createValidateEmailSuccess(String validEmail) {
        //given
        String username = "username";

        //when
        User user = assertDoesNotThrow(() -> User.create(validEmail, username));

        //then
        assertEquals(validEmail, user.getEmail());
        assertEquals(username, user.getName());
    }

    @DisplayName("create - validateEmail 테스트 - 이메일이 올바르지 않으면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"e2!naver.com", "d@;g.com", "gp@navercom",
        "sexy@gmail.naver.edsf,ewweef.dc.ad"})
    void createValidateEmailFail(String notValidEmail) {
        String username = "name!";
        // when & then
        assertThrows(IllegalArgumentException.class, () -> User.create(notValidEmail, username));
    }

    @DisplayName("create - validateUserName 테스트 - 유저 이름이 1자 이상 12자 이하이면 예외를 던지지 않늗다.")
    @ParameterizedTest
    @ValueSource(strings = {"1", "12", "123", "a", "ab", "s", "하", "이"})
    void createValidateUserNameSuccess(String validName) {
        String email = "hihi@naver.com";

        // when
        User user = assertDoesNotThrow(() -> User.create(email, validName));

        //then
        assertEquals(validName, user.getName());
        assertEquals(email, user.getEmail());
    }
}