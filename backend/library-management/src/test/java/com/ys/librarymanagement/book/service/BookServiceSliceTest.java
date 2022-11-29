package com.ys.librarymanagement.book.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.ys.librarymanagement.domain.book.api.request.BookCreateRequest;
import com.ys.librarymanagement.domain.book.domain.Book;
import com.ys.librarymanagement.domain.book.domain.BookStatus;
import com.ys.librarymanagement.domain.book.domain.BookType;
import com.ys.librarymanagement.domain.book.exception.DuplicateBookException;
import com.ys.librarymanagement.domain.book.repository.BookRepository;
import com.ys.librarymanagement.domain.book.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceSliceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @DisplayName("createSuccessTest - 책 이름과 책 타입이 등록되어있지 않다면 정상 등록된다.")
    @Test
    void createBookSuccess() {
        //given
        String bookName = "bookname";
        BookType bookType = BookType.COMPUTER;

        BookCreateRequest bookCreateRequest = new BookCreateRequest(bookName, bookType);

        Book book = Book.create(bookName, bookType);

        given(bookRepository.existsByNameAndBookType(bookName, bookType))
            .willReturn(false);

        given(bookRepository.save(book))
            .willReturn(book);

        //when
        Book createdBook = bookService.createBook(bookCreateRequest);

        //then
        assertEquals(BookStatus.RENTAL_AVAILABLE, createdBook.getBookStatus());
        assertEquals(bookType, createdBook.getBookType());
        assertEquals(bookName, createdBook.getName());

        verify(bookRepository).existsByNameAndBookType(bookName, bookType);
        verify(bookRepository).save(book);
    }

    @DisplayName("createFailTest - 책 이름과 책 타입이 등록되어있면 등록이 안되고 예외를 던진다")
    @Test
    void createBookFailDuplicateBook() {
        //given
        String bookName = "bookname";
        BookType bookType = BookType.COMPUTER;

        BookCreateRequest bookCreateRequest = new BookCreateRequest(bookName, bookType);

        given(bookRepository.existsByNameAndBookType(bookName, bookType))
            .willReturn(true);

        // when & then
        assertThrows(DuplicateBookException.class, () -> bookService.createBook(bookCreateRequest));

        verify(bookRepository).existsByNameAndBookType(bookName, bookType);
    }

}