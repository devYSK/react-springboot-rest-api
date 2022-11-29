package com.ys.librarymanagement.domain.book.api.response;

import com.ys.librarymanagement.domain.book.domain.Book;
import com.ys.librarymanagement.domain.book.domain.BookStatus;
import com.ys.librarymanagement.domain.book.domain.BookType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookResponse {

    private final Long id;

    private final String bookName;

    private final BookType bookType;

    private final BookStatus bookStatus;

    private final LocalDateTime createdAt;

    public static BookResponse of(Book book) {
        return new BookResponse(book.getId(), book.getName(), book.getBookType(), book.getBookStatus(), book.getCreatedAt());
    }

}
