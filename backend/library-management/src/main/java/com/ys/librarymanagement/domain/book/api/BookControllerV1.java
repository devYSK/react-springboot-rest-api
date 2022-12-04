package com.ys.librarymanagement.domain.book.api;

import com.ys.librarymanagement.domain.book.api.request.BookCreateRequest;
import com.ys.librarymanagement.domain.book.api.request.BookNameReturnRequest;
import com.ys.librarymanagement.domain.book.api.request.BookRentalRequest;
import com.ys.librarymanagement.domain.book.api.request.BookReturnRequest;
import com.ys.librarymanagement.domain.book.api.request.BookNameRentalRequest;
import com.ys.librarymanagement.domain.book.api.response.BookCreateResponse;
import com.ys.librarymanagement.domain.book.api.response.BookRentalResponse;
import com.ys.librarymanagement.domain.book.api.response.BookResponse;
import com.ys.librarymanagement.domain.book.service.BookService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        return new ResponseEntity<>(bookService.createBookAndGetResponse(request),
            HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> findAllBooks() {
        return ResponseEntity.ok(bookService.findAllBooks());
    }

    @PatchMapping("/{bookId}/rental")
    public ResponseEntity<BookRentalResponse> toRentalBook(
        @PathVariable Long bookId,
        @RequestBody BookRentalRequest request) {

        return ResponseEntity.ok(bookService.rentalBook(bookId, request.getUserId()));
    }

    @PatchMapping("/{bookId}/return")
    public ResponseEntity<BookRentalResponse> toReturnBook(
        @PathVariable Long bookId,
        @RequestBody BookReturnRequest request) {

        bookService.returnBook(bookId, request.getUserId());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/rental")
    public ResponseEntity<BookRentalResponse> toRentalBook(@RequestBody @Valid BookNameRentalRequest request) {

        return ResponseEntity.ok(bookService.rentalBook(request.getEmail(), request.getBookName()));
    }

    @PatchMapping("/return")
    public ResponseEntity<Void> toRentalBook(@RequestBody @Valid BookNameReturnRequest request) {
        bookService.returnBook(request.getEmail(), request.getBookName());

        return ResponseEntity.ok().build();
    }


}
