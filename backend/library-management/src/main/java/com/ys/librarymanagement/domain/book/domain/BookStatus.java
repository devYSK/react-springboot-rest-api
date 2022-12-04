package com.ys.librarymanagement.domain.book.domain;

public enum BookStatus {

    RENTAL_AVAILABLE("대여 가능"),
    RENTED("대여 중");

    private final String status;

    BookStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
