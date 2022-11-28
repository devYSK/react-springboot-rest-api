package com.ys.librarymanagement.user.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.librarymanagement.user.service.UserService;
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


    @DisplayName("post /api/v1/users - 유저 생성에 성공한다.")
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
            .andDo(print());

    }
}