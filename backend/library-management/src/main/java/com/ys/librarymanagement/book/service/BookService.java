package com.ys.librarymanagement.book.service;

import com.ys.librarymanagement.book.api.BookCreateRequest;
import com.ys.librarymanagement.book.api.BookCreateResponse;
import com.ys.librarymanagement.book.api.BookResponse;
import com.ys.librarymanagement.book.domain.Book;
import com.ys.librarymanagement.book.exception.DuplicateBookException;
import com.ys.librarymanagement.book.repository.BookRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public Book createBook(BookCreateRequest request) {
        if (bookRepository.existsByNameAndBookType(request.getBookName(), request.getBookType())) {
            throw new DuplicateBookException(request.getBookName(), request.getBookType().name());
        }

        Book book = Book.create(request.getBookName(), request.getBookType());
        return bookRepository.save(book);
    }

    @Transactional
    public BookCreateResponse createBookAndGetResponse(BookCreateRequest request) {
        Book book = createBook(request);

        return new BookCreateResponse(book.getId(), book.getName(), book.getBookType(),
            book.getBookStatus());
    }

    @Transactional
    public List<BookResponse> findAllBooks() {
        return bookRepository.findAll().stream().map(BookResponse::of)
            .collect(Collectors.toList());
    }
}
