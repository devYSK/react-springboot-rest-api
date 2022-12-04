package com.ys.librarymanagement.domain.user.api;

import com.ys.librarymanagement.domain.user.api.request.UserCreateRequest;
import com.ys.librarymanagement.domain.user.api.response.UserCreateResponse;
import com.ys.librarymanagement.domain.user.api.response.UserResponse;
import com.ys.librarymanagement.domain.user.service.UserService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserControllerV1 {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserCreateResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        return new ResponseEntity<>(userService.createUserAndGetResponse(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAllUsers() {
        List<UserResponse> userResponses = userService.findAllUsers();

        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/{userId}/rentals")
    public ResponseEntity<?> findAllUserRentals(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findAllUserRentals(userId));
    }

}
