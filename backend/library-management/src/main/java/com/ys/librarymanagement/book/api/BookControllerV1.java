package com.ys.librarymanagement.book.api;

import com.ys.librarymanagement.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/books")
@RestController
@RequiredArgsConstructor
public class BookControllerV1 {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookCreateResponse> createBook(@RequestBody BookCreateRequest request) {
        return new ResponseEntity<>(bookService.createBookAndGetResponse(request), HttpStatus.CREATED);
    }


}
