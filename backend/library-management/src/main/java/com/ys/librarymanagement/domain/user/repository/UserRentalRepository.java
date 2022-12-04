package com.ys.librarymanagement.domain.user.repository;

import com.ys.librarymanagement.domain.user.domain.UserRental;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRentalRepository extends JpaRepository<UserRental, Long> {

    Optional<UserRental> findByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @EntityGraph(attributePaths = {"user", "book"})
    List<UserRental> findAllByUserId(@Param("userId") Long userId);

}

