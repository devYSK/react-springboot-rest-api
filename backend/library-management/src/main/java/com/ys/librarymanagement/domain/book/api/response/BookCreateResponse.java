package com.ys.librarymanagement.domain.book.api.response;

import com.ys.librarymanagement.domain.book.domain.BookStatus;
import com.ys.librarymanagement.domain.book.domain.BookType;
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
