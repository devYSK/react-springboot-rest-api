package com.ys.librarymanagement.domain.user.repository;

import com.ys.librarymanagement.domain.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(@Param("email") String email);

    Optional<User> findByEmail(@Param("email") String email);

}
