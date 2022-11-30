package com.ys.librarymanagement.domain.book.service;

import com.ys.librarymanagement.common.exception.EntityNotFoundException;
import com.ys.librarymanagement.domain.book.api.request.BookCreateRequest;
import com.ys.librarymanagement.domain.book.api.response.BookCreateResponse;
import com.ys.librarymanagement.domain.book.api.response.BookRentalResponse;
import com.ys.librarymanagement.domain.book.api.response.BookResponse;
import com.ys.librarymanagement.domain.book.domain.Book;
import com.ys.librarymanagement.domain.book.exception.AlreadyRentedBookException;
import com.ys.librarymanagement.domain.book.exception.DuplicateBookException;
import com.ys.librarymanagement.domain.book.repository.BookRepository;
import com.ys.librarymanagement.domain.book_rental_history.BookRentalHistory;
import com.ys.librarymanagement.domain.book_rental_history.BookRentalHistoryRepository;
import com.ys.librarymanagement.domain.book_rental_history.RentalStatus;
import com.ys.librarymanagement.domain.user.domain.User;
import com.ys.librarymanagement.domain.user.repository.UserRepository;
import com.ys.librarymanagement.domain.user_rental_list.UserRental;
import com.ys.librarymanagement.domain.user_rental_list.UserRentalRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    private final BookRentalHistoryRepository bookRentalHistoryRepository;

    private final UserRentalRepository userRentalRepository;

    @Transactional
    public Book createBook(BookCreateRequest request) {
        if (bookRepository.existsByNameAndBookType(request.getBookName(), request.getBookType())) {
            throw new DuplicateBookException(request.getBookName(), request.getBookType().name());
        }

        Book book = Book.create(request.getBookName(), request.getBookType());
        return bookRepository.save(book);
    }

    @Transactional
    public BookCreateResponse createBookAndGetResponse(BookCreateRequest request) {
        Book book = createBook(request);

        return new BookCreateResponse(book.getId(), book.getName(), book.getBookType(),
            book.getBookStatus());
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findAllBooks() {
        return bookRepository.findAll().stream().map(BookResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public BookRentalResponse rentalBook(Long bookId, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException(User.class, userId));

        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException(Book.class, bookId));

        if (book.isRented()) {
            throw new AlreadyRentedBookException("이미 대여된 책입니다.");
        }

        book.toRental();

        UserRental userRental = UserRental.create(book, user);

        userRentalRepository.save(userRental);

        BookRentalHistory bookRentalHistory = BookRentalHistory.create(user, book, RentalStatus.RENTED);

        return new BookRentalResponse(bookRentalHistoryRepository.save(bookRentalHistory).getId());
    }

    @Transactional
    public void returnBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException(Book.class, bookId));

        if (!book.isRented()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "대여되지 않은 책입니다.");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException(User.class, userId));

        UserRental userRental = userRentalRepository.findByUserIdAndBookId(userId, bookId)
            .orElseThrow(
                () -> new HttpClientErrorException(HttpStatus.FORBIDDEN, "해당 유저가 대여한 책이 아닙니다."));

        book.toReturn();

        userRentalRepository.delete(userRental);

        BookRentalHistory bookRentalHistory = BookRentalHistory.create(user, book,
            RentalStatus.RETURNED);

        bookRentalHistoryRepository.save(bookRentalHistory);
    }
}
