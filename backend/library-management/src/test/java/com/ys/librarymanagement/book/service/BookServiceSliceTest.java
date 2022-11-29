package com.ys.librarymanagement.book.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import com.ys.librarymanagement.common.exception.EntityNotFoundException;
import com.ys.librarymanagement.domain.book.api.request.BookCreateRequest;
import com.ys.librarymanagement.domain.book.api.response.BookRentalResponse;
import com.ys.librarymanagement.domain.book.domain.Book;
import com.ys.librarymanagement.domain.book.domain.BookStatus;
import com.ys.librarymanagement.domain.book.domain.BookType;
import com.ys.librarymanagement.domain.book.exception.AlreadyRentedBookException;
import com.ys.librarymanagement.domain.book.exception.DuplicateBookException;
import com.ys.librarymanagement.domain.book.repository.BookRepository;
import com.ys.librarymanagement.domain.book.service.BookService;
import com.ys.librarymanagement.domain.book_rental_history.BookRentalHistory;
import com.ys.librarymanagement.domain.book_rental_history.BookRentalHistoryRepository;
import com.ys.librarymanagement.domain.user.domain.User;
import com.ys.librarymanagement.domain.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceSliceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRentalHistoryRepository bookRentalHistoryRepository;

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

    @DisplayName("rentalBook Test - 책 대여에 성공한다.")
    @Test
    void rentalBookSuccess() {
        //given
        Book book = Book.create("bookName", BookType.COMPUTER);
        User user = User.create("testmail@naver.com", "testEmail!");
        BookRentalHistory bookRentalHistory = BookRentalHistory.create(user, book);
        Long bookId = 0L;
        Long userId = 0L;

        given(userRepository.findById(userId))
            .willReturn(Optional.of(user));

        given(bookRepository.findById(bookId))
            .willReturn(Optional.ofNullable(book));

        given(bookRentalHistoryRepository.save(bookRentalHistory))
            .willReturn(bookRentalHistory);

        try (MockedStatic<BookRentalHistory> bookRentalHistoryMock = mockStatic(
            BookRentalHistory.class)) {

            given(BookRentalHistory.create(user, book))
                .willReturn(bookRentalHistory);

            //when
            BookRentalResponse bookRentalResponse = bookService.rentalBook(bookId, userId);

            //then
            assertEquals(bookRentalHistory.getId(), bookRentalResponse.getBookRentalHistoryId());

            verify(userRepository).findById(userId);
            verify(bookRepository).findById(bookId);
            verify(bookRentalHistoryRepository).save(bookRentalHistory);
            bookRentalHistoryMock.verify(() -> BookRentalHistory.create(user, book));
        }
    }

    @DisplayName("rentalBook Test - 유저가 없으므로 책 대여에 실패한다.")
    @Test
    void rentalBookFailNotFoundUser() {
        //given
        Long bookId = 0L;
        Long userId = 0L;

        //when & then
        assertThrows(EntityNotFoundException.class, () -> bookService.rentalBook(bookId, userId));
    }

    @DisplayName("rentalBook Test - 책이 없으므로 책 대여에 실패한다.")
    @Test
    void rentalBookFailNotFoundBook() {
        //given
        Long bookId = 0L;
        Long userId = 0L;

        //when & then
        assertThrows(EntityNotFoundException.class, () -> bookService.rentalBook(bookId, userId));
    }


    @DisplayName("rentalBook Test - 책이 이미 대여되어 있으므로 대여에 실패한다.")
    @Test
    void rentalBookFailAlreadyRented() {
        //given
        Book book = Book.create("bookName", BookType.COMPUTER);
        User user = User.create("testmail@naver.com", "testEmail!");
        book.toRental();
        Long bookId = 0L;
        Long userId = 0L;

        given(userRepository.findById(userId))
            .willReturn(Optional.of(user));

        given(bookRepository.findById(bookId))
            .willReturn(Optional.ofNullable(book));

        //when
        assertThrows(AlreadyRentedBookException.class,
            () -> bookService.rentalBook(bookId, userId));

        //then
        verify(userRepository).findById(userId);
        verify(bookRepository).findById(bookId);
    }


}