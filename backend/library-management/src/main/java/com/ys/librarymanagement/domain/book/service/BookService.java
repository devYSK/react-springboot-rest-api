package com.ys.librarymanagement.domain.book.service;

import com.ys.librarymanagement.common.exception.EntityNotFoundException;
import com.ys.librarymanagement.domain.book.api.request.BookCreateRequest;
import com.ys.librarymanagement.domain.book.api.request.NotUserRentedBookException;
import com.ys.librarymanagement.domain.book.api.response.BookCreateResponse;
import com.ys.librarymanagement.domain.book.api.response.BookRentalResponse;
import com.ys.librarymanagement.domain.book.api.response.BookResponse;
import com.ys.librarymanagement.domain.book.domain.Book;
import com.ys.librarymanagement.domain.book.exception.AlreadyRentedBookException;
import com.ys.librarymanagement.domain.book.exception.DuplicateBookException;
import com.ys.librarymanagement.domain.book.repository.BookRepository;
import com.ys.librarymanagement.domain.book.domain.BookRentalHistory;
import com.ys.librarymanagement.domain.book.repository.BookRentalHistoryRepository;
import com.ys.librarymanagement.domain.book.domain.RentalStatus;
import com.ys.librarymanagement.domain.user.domain.User;
import com.ys.librarymanagement.domain.user.repository.UserRepository;
import com.ys.librarymanagement.domain.user.domain.UserRental;
import com.ys.librarymanagement.domain.user.repository.UserRentalRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        book.toReturn();

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException(User.class, userId));

        UserRental userRental = userRentalRepository.findByUserIdAndBookId(userId, bookId)
            .orElseThrow(
                () -> new NotUserRentedBookException("해당 유저가 대여한 책이 아닙니다."));


        userRentalRepository.delete(userRental);

        BookRentalHistory bookRentalHistory = BookRentalHistory.create(user, book,
            RentalStatus.RETURNED);

        bookRentalHistoryRepository.save(bookRentalHistory);
    }

    @Transactional
    public BookRentalResponse rentalBook(String email, String bookName) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException(User.class, email));

        Book book = bookRepository.findByName(bookName)
            .orElseThrow(() -> new EntityNotFoundException(Book.class, bookName));

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
    public void returnBook(String email, String bookName) {
        Book book = bookRepository.findByName(bookName)
            .orElseThrow(() -> new EntityNotFoundException(Book.class, bookName));

        book.toReturn();

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException(User.class, email));

        UserRental userRental = userRentalRepository.findByUserIdAndBookId(user.getId(), book.getId())
            .orElseThrow(
                () -> new NotUserRentedBookException("해당 유저가 대여한 책이 아닙니다."));


        userRentalRepository.delete(userRental);

        BookRentalHistory bookRentalHistory = BookRentalHistory.create(user, book,
            RentalStatus.RETURNED);

        bookRentalHistoryRepository.save(bookRentalHistory);
    }

}
