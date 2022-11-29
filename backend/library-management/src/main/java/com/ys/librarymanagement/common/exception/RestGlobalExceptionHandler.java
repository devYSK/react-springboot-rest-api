package com.ys.librarymanagement.common.exception;

import com.ys.librarymanagement.common.response.ErrorResponse;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestGlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> defaultHandle(Exception e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timeStamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .message(e.getMessage())
            .requestUrl(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> entityNotFoundHandle(EntityNotFoundException e,
        HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timeStamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .message(e.getMessage())
            .requestUrl(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> duplicateEmailHandle(DuplicateException e,
        HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timeStamp(LocalDateTime.now())
            .status(HttpStatus.CONFLICT.value())
            .message(e.getMessage())
            .requestUrl(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
