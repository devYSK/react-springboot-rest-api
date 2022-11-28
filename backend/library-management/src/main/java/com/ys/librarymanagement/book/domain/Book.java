package com.ys.librarymanagement.book.domain;

import com.ys.librarymanagement.common.base.AbstractTimeColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor
public class Book extends AbstractTimeColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BookType bookType;

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

    public void toRental() {
        bookStatus = BookStatus.RENTED;
    }

    public void toReturn() {
        bookStatus = BookStatus.RENTAL_AVAILABLE;
    }

    private static void validateBookName(String bookName) {
        if (!StringUtils.hasText(bookName)) {
            throw new IllegalArgumentException("책 이름은 비어있어선 안됩니다." + bookName);
        }
    }
}
