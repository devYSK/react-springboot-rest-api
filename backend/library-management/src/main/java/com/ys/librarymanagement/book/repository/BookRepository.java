package com.ys.librarymanagement.book.repository;

import com.ys.librarymanagement.book.domain.Book;
import com.ys.librarymanagement.book.domain.BookStatus;
import com.ys.librarymanagement.book.domain.BookType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByNameAndBookType(@Param("name") String name, @Param("bookType") BookType bookType);

}
