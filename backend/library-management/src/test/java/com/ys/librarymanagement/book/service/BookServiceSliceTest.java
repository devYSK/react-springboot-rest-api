package com.ys.librarymanagement.book.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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
import com.ys.librarymanagement.domain.book_rental_history.RentalStatus;
import com.ys.librarymanagement.domain.user.domain.User;
import com.ys.librarymanagement.domain.user.repository.UserRepository;
import com.ys.librarymanagement.domain.user_rental_list.UserRental;
import com.ys.librarymanagement.domain.user_rental_list.UserRentalRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

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

    @Mock
    private UserRentalRepository userRentalRepository;

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
        BookRentalHistory bookRentalHistory = BookRentalHistory.create(user, book,
            RentalStatus.RENTED);

        UserRental userRental = UserRental.create(book, user);

        Long bookId = 0L;
        Long userId = 0L;

        given(userRepository.findById(userId))
            .willReturn(Optional.of(user));

        given(bookRepository.findById(bookId))
            .willReturn(Optional.ofNullable(book));

        given(bookRentalHistoryRepository.save(bookRentalHistory))
            .willReturn(bookRentalHistory);

        given(userRentalRepository.save(userRental))
            .willReturn(userRental);

        try (MockedStatic<BookRentalHistory> bookRentalHistoryMock = mockStatic(
            BookRentalHistory.class);
            MockedStatic<UserRental> userRentalMock = mockStatic(UserRental.class)) {

            given(BookRentalHistory.create(user, book, RentalStatus.RENTED))
                .willReturn(bookRentalHistory);

            given(UserRental.create(book, user))
                .willReturn(userRental);

            //when
            BookRentalResponse bookRentalResponse = bookService.rentalBook(bookId, userId);

            //then
            assertEquals(bookRentalHistory.getId(), bookRentalResponse.getBookRentalHistoryId());

            verify(userRepository).findById(userId);
            verify(bookRepository).findById(bookId);
            verify(bookRentalHistoryRepository).save(bookRentalHistory);
            verify(userRentalRepository).save(userRental);

            bookRentalHistoryMock.verify(() -> BookRentalHistory.create(user, book,
                RentalStatus.RENTED));
            userRentalMock.verify(() -> UserRental.create(book, user));
        }
    }

    @DisplayName("rentalBook Test - 책이 대여중이므로 대여에 실패한다.")
    @Test
    void rentalBookFailIsRented() {
        //given
        Book book = Book.create("bookName", BookType.COMPUTER);
        book.toRental();
        User user = User.create("testmail@naver.com", "testEmail!");

        Long bookId = 0L;
        Long userId = 0L;

        given(userRepository.findById(userId))
            .willReturn(Optional.of(user));

        given(bookRepository.findById(bookId))
            .willReturn(Optional.of(book));

        //when & then
        assertThrows(AlreadyRentedBookException.class,
            () -> bookService.rentalBook(bookId, userId));

        verify(userRepository).findById(userId);
        verify(bookRepository).findById(bookId);

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

    @DisplayName("returnBook Test - 책이 없으므로 책 반납에 실패한다.")
    @Test
    void returnBookFailNotFoundBoot() {
        //given
        Long bookId = 0L;
        Long userId = 0L;
        //when & then
        assertThrows(EntityNotFoundException.class, () -> bookService.rentalBook(bookId, userId));
    }

    @DisplayName("returnBook Test - 책이 대여상태가 아니므로 책 반납에 실패한다.")
    @Test
    void returnBookFailNotRentedStatus() {
        //given
        Long bookId = 0L;
        Long userId = 0L;
        Book book = Book.create("bookName", BookType.COMPUTER);

        given(bookRepository.findById(bookId))
            .willReturn(Optional.of(book));

        //when & then
        assertThrows(HttpClientErrorException.class, () -> bookService.returnBook(bookId, userId));

        verify(bookRepository).findById(bookId);
    }

    @DisplayName("returnBook Test - 유저가 없으므로 책 반납에 실패한다.")
    @Test
    void returnBookFailNotFoundUser() {
        //given
        Long bookId = 0L;
        Long userId = 0L;
        Book book = Book.create("bookName", BookType.COMPUTER);
        book.toRental();
        given(bookRepository.findById(bookId))
            .willReturn(Optional.of(book));

        given(userRepository.findById(userId))
            .willReturn(Optional.empty());

        //when & then
        assertThrows(EntityNotFoundException.class, () -> bookService.returnBook(bookId, userId));

        verify(bookRepository).findById(bookId);
        verify(userRepository).findById(userId);
    }

    @DisplayName("returnBook Test - 유저가 빌렸던 책이 없으므로 책 반납에 실패한다.")
    @Test
    void returnBookFailNotFondUserRental() {
        //given
        Long bookId = 0L;
        Long userId = 0L;
        Book book = Book.create("bookName", BookType.COMPUTER);
        book.toRental();
        User user = User.create("user1@naver.com", "userName");
        given(bookRepository.findById(bookId))
            .willReturn(Optional.of(book));

        given(userRepository.findById(userId))
            .willReturn(Optional.of(user));

        given(userRentalRepository.findByUserIdAndBookId(userId, bookId))
            .willReturn(Optional.empty());

        //when & then
        assertThrows(HttpClientErrorException.class, () -> bookService.returnBook(bookId, userId));

        verify(bookRepository).findById(bookId);
        verify(userRepository).findById(userId);
        verify(userRentalRepository).findByUserIdAndBookId(userId, bookId);
    }

    @DisplayName("returnBookTest - 책 반납에 성공한다.")
    @Test
    void returnBookSuccess() {
        //given
        Long bookId = 0L;
        Long userId = 0L;
        Book book = Book.create("bookName", BookType.COMPUTER);
        book.toRental();
        User user = User.create("user1@naver.com", "userName");

        UserRental userRental = UserRental.create(book, user);

        BookRentalHistory bookRentalHistory = BookRentalHistory.create(user, book,
            RentalStatus.RETURNED);

        given(bookRepository.findById(bookId))
            .willReturn(Optional.of(book));

        given(userRepository.findById(userId))
            .willReturn(Optional.of(user));

        given(userRentalRepository.findByUserIdAndBookId(userId, bookId))
            .willReturn(Optional.of(userRental));

        willDoNothing().given(userRentalRepository)
            .delete(userRental);

        given(bookRentalHistoryRepository.save(bookRentalHistory))
            .willReturn(bookRentalHistory);

        //when

        try (MockedStatic<BookRentalHistory> bookRentalHistoryMock = mockStatic(
            BookRentalHistory.class)) {

            given(BookRentalHistory.create(user, book, RentalStatus.RETURNED))
                .willReturn(bookRentalHistory);

            bookService.returnBook(bookId, userId);
            //then
            assertEquals(BookStatus.RENTAL_AVAILABLE, book.getBookStatus());

            verify(bookRepository).findById(bookId);
            verify(userRepository).findById(userId);
            verify(userRentalRepository).findByUserIdAndBookId(userId, bookId);
            verify(userRentalRepository).delete(userRental);
            verify(bookRentalHistoryRepository).save(bookRentalHistory);

            bookRentalHistoryMock.verify(() -> BookRentalHistory.create(user, book, RentalStatus.RETURNED));

        }

    }

}