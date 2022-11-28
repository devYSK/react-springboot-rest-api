package com.ys.librarymanagement.user.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super(String.format("%s 이메일은 이미 존재합니다.", email));
    }

}
