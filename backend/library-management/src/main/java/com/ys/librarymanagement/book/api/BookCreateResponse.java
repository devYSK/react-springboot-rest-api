package com.ys.librarymanagement.book.api;

import com.ys.librarymanagement.book.domain.BookStatus;
import com.ys.librarymanagement.book.domain.BookType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookCreateResponse {

    private final Long bookId;

    private final String bookName;

    private final BookType bookType;

    private final BookStatus bookStatus;

}
