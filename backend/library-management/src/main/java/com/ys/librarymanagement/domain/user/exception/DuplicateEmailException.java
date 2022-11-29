package com.ys.librarymanagement.domain.user.exception;

import com.ys.librarymanagement.common.exception.DuplicateException;

public class DuplicateEmailException extends DuplicateException {

    public DuplicateEmailException(String email) {
        super(String.format("%s 이메일은 이미 존재합니다.", email));
    }

}
