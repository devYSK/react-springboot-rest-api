package com.ys.librarymanagement.domain.user.api.response;

import com.ys.librarymanagement.domain.book.api.response.BookResponse;
import com.ys.librarymanagement.domain.book.domain.Book;
import com.ys.librarymanagement.domain.user.domain.User;
import com.ys.librarymanagement.domain.user.domain.UserRental;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class UserRentalsResponse {

    private Long userId;

    private String email;

    private String name;

    private List<BookResponse> books;

    public UserRentalsResponse(User user, List<Book> books) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.books = books.stream().map(BookResponse::of).collect(Collectors.toList());
    }


    public UserRentalsResponse(Map<User, List<UserRental>> userRentalMap) {

        User user = userRentalMap.keySet().iterator().next();

        List<UserRental> userRentals = userRentalMap.get(user);

        this.userId = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.books = userRentals.stream()
            .map(UserRental::getBook)
            .map(BookResponse::of)
            .collect(Collectors.toList());
    }

}
