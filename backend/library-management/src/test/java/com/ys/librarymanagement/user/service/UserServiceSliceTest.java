package com.ys.librarymanagement.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import com.ys.librarymanagement.domain.user.api.request.UserCreateRequest;
import com.ys.librarymanagement.domain.user.domain.User;
import com.ys.librarymanagement.domain.user.exception.DuplicateEmailException;
import com.ys.librarymanagement.domain.user.repository.UserRepository;
import com.ys.librarymanagement.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
class UserServiceSliceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @DisplayName("createSuccessTest - 요청으로 들어온 이메일이 등록되어 있지 않으면 생성에 성공한다. ")
    @Test
    void createSuccess() {
        //given
        String email = "testemail@naver.com";
        String name = "testname";

        User user = new User(email, name);

        try (MockedStatic<User> mockUser = mockStatic(User.class)) {
            UserCreateRequest userCreateRequest = new UserCreateRequest(email, name);

            given(userRepository.existsByEmail(email))
                .willReturn(false);

            given(User.create(email, name))
                .willReturn(user);

            given(userRepository.save(user))
                .willReturn(user);

            //when
            User createdUser = userService.createUser(userCreateRequest);

            //then
            assertEquals(user, createdUser);
            assertEquals(email, createdUser.getEmail());
            assertEquals(name, createdUser.getName());

            verify(userRepository).existsByEmail(email);
            mockUser.verify(() -> User.create(email, name));
            verify(userRepository).save(user);
        }

    }

    @DisplayName("createFailTest - 요청으로 들어온 이메일이 등록되어 있으면 생성에 실패하고 예외를 던진다. ")
    @Test
    void createFailDuplicateEmail() {
        //given
        String email = "testemail@naver.com";
        String name = "testname";

        UserCreateRequest userCreateRequest = new UserCreateRequest(email, name);

        given(userRepository.existsByEmail(email))
            .willReturn(true);

        // when
        assertThrows(DuplicateEmailException.class,
            () -> userService.createUser(userCreateRequest));

        //then
        verify(userRepository).existsByEmail(email);
    }


}