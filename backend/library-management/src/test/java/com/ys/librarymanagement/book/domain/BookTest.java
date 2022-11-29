package com.ys.librarymanagement.book.domain;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ys.librarymanagement.domain.book.domain.Book;
import com.ys.librarymanagement.domain.book.domain.BookStatus;
import com.ys.librarymanagement.domain.book.domain.BookType;
import com.ys.librarymanagement.domain.book.exception.AlreadyRentedBookException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BookTest {

    @DisplayName("create 테스트 - 책 이름이 비어있지 않으면 책 생성에 성공한다.")
    @Test
    void createSuccess() {
        //given
        String bookName = "1";
        BookType bookType = BookType.COMPUTER;

        //when
        Book createBook = assertDoesNotThrow(() -> Book.create(bookName, bookType));

        //then
        assertEquals(bookName, createBook.getName());
    }

    @DisplayName("create 테스트 - 책 이름이 비어있으면 책 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {" ", "", "  "})
    void createFail(String emptyBookName) {
        //given
        String bookName = "";
        BookType bookType = BookType.COMPUTER;

        //when & then
        assertThrows(IllegalArgumentException.class, () -> Book.create(bookName, bookType));
    }

    @DisplayName("create 테스트 - 책이 처음 생성되면 BookStatus RENTAL_AVAILABLE 이다. ")
    @Test
    void createDefaultBookStatusRENTAL_AVAILABLE() {
        //given
        String bookName = "bookName";
        BookType bookType = BookType.COMPUTER;

        //when & then
        Book book = Book.create(bookName, bookType);

        // then
        assertEquals(BookStatus.RENTAL_AVAILABLE, book.getBookStatus());
    }

    @DisplayName("toRental 테스트 - toRental 을 호출하면 Status가 RENTED로 바뀐다.")
    @Test
    void toRental() {
        //given
        String bookName = "bookName";
        BookType bookType = BookType.COMPUTER;

        Book book = Book.create(bookName, bookType);

        BookStatus beforeCallStatus = book.getBookStatus();

        //when
        book.toRental();

        //then
        BookStatus afterCallStatus = book.getBookStatus();
        assertNotEquals(beforeCallStatus, afterCallStatus);
        assertEquals(BookStatus.RENTED, afterCallStatus);
    }

    @DisplayName("toRental 테스트 - toReturn 을 호출하면 Status가 RENTAL_AVAILABLE 로 바뀐다.")
    @Test
    void toReturnSuccess() {
        //given
        String bookName = "bookName";
        BookType bookType = BookType.COMPUTER;

        Book book = Book.create(bookName, bookType);

        book.toRental();
        BookStatus beforeCallStatus = book.getBookStatus();
        //when
        book.toReturn();

        //then
        BookStatus afterCallStatus = book.getBookStatus();
        assertNotEquals(beforeCallStatus, afterCallStatus);
        assertEquals(BookStatus.RENTAL_AVAILABLE, afterCallStatus);
    }

    @DisplayName("toRental 테스트 - toReturn 을 호출했을 때 Status가 RENTED 이면 예외를 던진다.")
    @Test
    void toReturnFailThrowException() {
        //given
        String bookName = "bookName";
        BookType bookType = BookType.COMPUTER;

        Book book = Book.create(bookName, bookType);

        book.toRental();
        //when & then
        assertThrows(AlreadyRentedBookException.class, book::toRental);
    }

}