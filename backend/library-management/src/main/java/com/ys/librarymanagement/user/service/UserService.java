package com.ys.librarymanagement.user.service;

import com.ys.librarymanagement.user.api.UserCreateRequest;
import com.ys.librarymanagement.user.api.UserCreateResponse;
import com.ys.librarymanagement.user.domain.User;
import com.ys.librarymanagement.user.exception.DuplicateEmailException;
import com.ys.librarymanagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(UserCreateRequest request) {

        validateExistsEmail(request);

        User createUser = User.create(request.getEmail(), request.getName());

        return userRepository.save(createUser);
    }

    @Transactional
    public UserCreateResponse createUserAndGetResponse(UserCreateRequest request) {
        User user = createUser(request);

        return new UserCreateResponse(user.getId(), user.getEmail(), user.getName());
    }

    private void validateExistsEmail(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }
    }

}
