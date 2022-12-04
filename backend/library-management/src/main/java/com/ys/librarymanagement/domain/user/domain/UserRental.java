package com.ys.librarymanagement.domain.user.domain;

import com.ys.librarymanagement.common.base.AbstractTimeColumn;
import com.ys.librarymanagement.domain.book.domain.Book;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(
            name = "user_book_unique_key",
            columnNames = {"user_id", "book_id"}
        )
    }
)
public class UserRental extends AbstractTimeColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserRental(Book book, User user) {
        this.book = book;
        this.user = user;
    }

    public static UserRental create(Book book, User user) {
        return new UserRental(book, user);
    }

}
