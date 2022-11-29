package com.ys.librarymanagement.domain.user.repository;

import com.ys.librarymanagement.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

}
