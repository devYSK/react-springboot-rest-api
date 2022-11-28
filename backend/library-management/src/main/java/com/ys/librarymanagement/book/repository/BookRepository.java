package com.ys.librarymanagement.book.repository;

import com.ys.librarymanagement.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
