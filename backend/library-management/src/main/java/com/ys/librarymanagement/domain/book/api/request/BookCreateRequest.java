package com.ys.librarymanagement.domain.book.api.request;

import com.ys.librarymanagement.domain.book.domain.BookType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class BookCreateRequest {

    private String bookName;

    private BookType bookType;

}
