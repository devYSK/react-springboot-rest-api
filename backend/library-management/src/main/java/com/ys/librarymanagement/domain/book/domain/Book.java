package com.ys.librarymanagement.domain.book.domain;

import com.ys.librarymanagement.common.base.AbstractTimeColumn;
import com.ys.librarymanagement.domain.book.exception.AlreadyRentedBookException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class Book extends AbstractTimeColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookType bookType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus bookStatus = BookStatus.RENTAL_AVAILABLE;

    @Builder
    public Book(String bookName, BookType bookType) {
        validateBookName(bookName);
        this.name = bookName;
        this.bookType = bookType;
    }

    public static Book create(String bookName, BookType bookType) {
        validateBookName(bookName);
        return Book.builder()
            .bookName(bookName)
            .bookType(bookType)
            .build();
    }

    public boolean isRented() {
        return this.bookStatus == BookStatus.RENTED;
    }

    public void toRental() {
        if (this.bookStatus == BookStatus.RENTED) {
            throw new AlreadyRentedBookException("이미 대여된 책입니다.");
        }

        bookStatus = BookStatus.RENTED;
    }

    public void toReturn() {
        if (!this.isRented()) {
            throw new IllegalStateException("대여되지 않은 책입니다.");
        }
        bookStatus = BookStatus.RENTAL_AVAILABLE;
    }

    private static void validateBookName(String bookName) {
        if (!StringUtils.hasText(bookName)) {
            throw new IllegalArgumentException("책 이름은 비어있어선 안됩니다." + bookName);
        }
    }
}
