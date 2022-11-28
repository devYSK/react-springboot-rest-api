package com.ys.librarymanagement.book.exception;

import com.ys.librarymanagement.common.exception.DuplicateException;

public class DuplicateBookException extends DuplicateException {

    public DuplicateBookException(String bookName, String bookType) {
        super(String.format("같은 이름과 같은 타입의 책이 존재합니다. bookName : %s, bookType : %s ", bookName, bookType ));
    }

}
