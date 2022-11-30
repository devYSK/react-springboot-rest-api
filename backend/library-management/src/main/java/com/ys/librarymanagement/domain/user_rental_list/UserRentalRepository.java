package com.ys.librarymanagement.domain.user_rental_list;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRentalRepository extends JpaRepository<UserRental, Long> {

    Optional<UserRental> findByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);

}
