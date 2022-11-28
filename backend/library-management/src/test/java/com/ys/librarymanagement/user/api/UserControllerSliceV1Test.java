package com.ys.librarymanagement.user.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.librarymanagement.common.exception.EntityNotFoundException;
import com.ys.librarymanagement.user.domain.User;
import com.ys.librarymanagement.user.exception.DuplicateEmailException;
import com.ys.librarymanagement.user.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserControllerV1.class)
class UserControllerSliceV1Test {

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @DisplayName("post /api/v1/users - 201 - 유저 생성에 성공한다.")
    @Test
    void createSuccess201() throws Exception {
        //given
        String email = "testEmail@email.com";
        String name = "test";
        UserCreateRequest request = new UserCreateRequest(email, name);
        UserCreateResponse response = new UserCreateResponse(0L, email, name);

        given(userService.createUserAndGetResponse(request))
            .willReturn(response);

        //when & then
        mockmvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isCreated())
            .andExpect(jsonPath("$.userId").exists())
            .andExpect(jsonPath("$.email").value(email))
            .andExpect(jsonPath("$.name").value(name))
            .andDo(print());

    }

    @DisplayName("post /api/v1/users - 400 - 이메일이 중복되어 유저 생성에 실패한다.")
    @Test
    void createFail400DuplicateEmail() throws Exception {
        //given
        String email = "testEmail@email.com";
        String name = "test";
        UserCreateRequest request = new UserCreateRequest(email, name);

        given(userService.createUserAndGetResponse(request))
            .willThrow(DuplicateEmailException.class);

        //when & then
        mockmvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isBadRequest())
            .andDo(print());
    }

    @DisplayName("get /api/v1/users - 200 - 저장된 모든 유저가 조회된다")
    @Test
    void findSuccess200() throws Exception {
        //given
        int size = 10;
        List<UserResponse> userResponse = createUserResponse(size);

        given(userService.findAllUsers())
            .willReturn(userResponse);

        //when
        mockmvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(size));
    }

    @DisplayName("get /api/v1/users - 404 - 유저가 없으므로 notfound 응답을 보내준다")
    @Test
    void findFail404() throws Exception {
        //given
        int size = 10;
        List<UserResponse> userResponse = createUserResponse(size);

        given(userService.findAllUsers())
            .willThrow(EntityNotFoundException.class);

        //when
        mockmvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }


    private List<UserResponse> createUserResponse(int size) {
        String email = "userEmail@naver.com";
        String name = "username";

        return IntStream.range(0, size)
            .mapToObj(value -> {
                User user = new User(value + email, value + name);

                return UserResponse.of(user);
            }).collect(Collectors.toList());
    }

}