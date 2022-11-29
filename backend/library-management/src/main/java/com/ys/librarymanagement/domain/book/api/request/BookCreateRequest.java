package com.ys.librarymanagement.domain.book.api.request;

import com.ys.librarymanagement.domain.book.domain.BookType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class BookCreateRequest {

    private final String bookName;

    private final BookType bookType;

}
