package com.ys.librarymanagement.domain.book.exception;

public class AlreadyRentedBookException extends RuntimeException{

    public AlreadyRentedBookException(String message) {
        super(message);
    }

}
