package com.ys.librarymanagement.domain.book_rental_history;

import com.ys.librarymanagement.common.base.AbstractTimeColumn;
import com.ys.librarymanagement.domain.book.domain.Book;
import com.ys.librarymanagement.domain.user.domain.User;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookRentalHistory extends AbstractTimeColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    private RentalStatus rentalStatus;

    protected BookRentalHistory(User user, Book book, RentalStatus rentalStatus) {
        this.user = user;
        this.book = book;
        this.rentalStatus = rentalStatus;
    }

    public static BookRentalHistory create(User user, Book book, RentalStatus rentalStatus) {
        return new BookRentalHistory(user, book, rentalStatus);
    }

}
