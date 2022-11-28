package com.ys.librarymanagement.book.api;

import com.ys.librarymanagement.book.domain.BookType;
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
