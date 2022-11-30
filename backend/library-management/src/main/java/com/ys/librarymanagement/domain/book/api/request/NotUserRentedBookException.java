package com.ys.librarymanagement.domain.book.api.request;

public class NotUserRentedBookException extends RuntimeException {

    public NotUserRentedBookException(String message) {
        super(message);
    }
}
