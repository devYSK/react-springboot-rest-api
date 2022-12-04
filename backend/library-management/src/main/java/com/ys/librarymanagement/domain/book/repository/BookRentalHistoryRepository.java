package com.ys.librarymanagement.domain.book.repository;

import com.ys.librarymanagement.domain.book.domain.BookRentalHistory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRentalHistoryRepository extends JpaRepository<BookRentalHistory, Long> {

    @Query("select brh from BookRentalHistory brh join fetch Book b "
        + "where brh.book.id = :bookId and brh.user.id = :userId")
    Optional<BookRentalHistory> findByBookIdAndUserIdWithBook(@Param("bookId") Long bookId,
        @Param("userId") Long userId);


}
