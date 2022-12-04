package com.ys.librarymanagement.domain.user.service;

import com.ys.librarymanagement.common.exception.EntityNotFoundException;
import com.ys.librarymanagement.domain.user.api.response.UserRentalsResponse;
import com.ys.librarymanagement.domain.user.domain.UserRental;
import com.ys.librarymanagement.domain.user.exception.DuplicateEmailException;
import com.ys.librarymanagement.domain.user.api.request.UserCreateRequest;
import com.ys.librarymanagement.domain.user.api.response.UserCreateResponse;
import com.ys.librarymanagement.domain.user.api.response.UserResponse;
import com.ys.librarymanagement.domain.user.domain.User;
import com.ys.librarymanagement.domain.user.repository.UserRentalRepository;
import com.ys.librarymanagement.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserRentalRepository userRentalRepository;

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

    @Transactional(readOnly = true)
    public List<UserResponse> findAllUsers() {

        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new EntityNotFoundException(User.class);
        }

        return users.stream()
            .map(UserResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserRentalsResponse findAllUserRentals(Long userId) {

        List<UserRental> userRentals = userRentalRepository.findAllByUserId(userId);

        Map<User, List<UserRental>> userRentalMap = userRentals.stream()
            .collect(Collectors.groupingBy(UserRental::getUser));

        if (userRentalMap.isEmpty()) {
            throw new EntityNotFoundException(UserRental.class, userId);
        }

        return new UserRentalsResponse(userRentalMap);
    }
}
