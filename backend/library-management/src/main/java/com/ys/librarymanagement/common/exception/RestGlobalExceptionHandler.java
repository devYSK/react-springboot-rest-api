package com.ys.librarymanagement.common.exception;

import com.ys.librarymanagement.common.response.ErrorResponse;
import com.ys.librarymanagement.domain.book.api.request.NotUserRentedBookException;
import com.ys.librarymanagement.domain.book.exception.AlreadyRentedBookException;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class RestGlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> httpClientErrorHandle(HttpClientErrorException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timeStamp(LocalDateTime.now())
            .status(e.getRawStatusCode())
            .message(e.getMessage())
            .requestUrl(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResponse, e.getStatusCode());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> illegalStateHandle(IllegalStateException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timeStamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .message(e.getMessage())
            .requestUrl(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> defaultHandle(MethodArgumentNotValidException e, HttpServletRequest request) {

        ErrorResponse errorResponse = makeErrorResponseWithBindingResult(e.getBindingResult());
        errorResponse.setRequestUrl(request.getRequestURI());

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

    @ExceptionHandler(NotUserRentedBookException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> notUserRentedBookHandle(NotUserRentedBookException e,
        HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timeStamp(LocalDateTime.now())
            .status(HttpStatus.FORBIDDEN.value())
            .message(e.getMessage())
            .requestUrl(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
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

    @ExceptionHandler(AlreadyRentedBookException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> rentalDeniedHandle(
        AlreadyRentedBookException e,
        HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timeStamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .message(e.getMessage())
            .requestUrl(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    private ErrorResponse makeErrorResponseWithBindingResult(BindingResult bindingResult) {
        String code;

        System.out.println("CommonControllerAdvice.makeErrorResponseWithBindingResult");

        if (bindingResult.getFieldError() == null) {
            code = bindingResult.getFieldErrors().toString();
        } else {
            code = Objects.requireNonNull(bindingResult.getFieldError()).getCode();
        }

        StringBuilder builder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("] - ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" . input value : [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]. ");
        }

        return ErrorResponse.builder()
            .timeStamp(LocalDateTime.now())
            .message(builder.toString())
            .status(HttpStatus.BAD_REQUEST.value())
            .build();
    }

}
