package com.ys.librarymanagement.book.api;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.librarymanagement.common.exception.EntityNotFoundException;
import com.ys.librarymanagement.domain.book.api.BookControllerV1;
import com.ys.librarymanagement.domain.book.api.request.BookCreateRequest;
import com.ys.librarymanagement.domain.book.api.request.BookRentalRequest;
import com.ys.librarymanagement.domain.book.api.request.BookReturnRequest;
import com.ys.librarymanagement.domain.book.api.request.NotUserRentedBookException;
import com.ys.librarymanagement.domain.book.api.response.BookCreateResponse;
import com.ys.librarymanagement.domain.book.api.response.BookRentalResponse;
import com.ys.librarymanagement.domain.book.api.response.BookResponse;
import com.ys.librarymanagement.domain.book.domain.Book;
import com.ys.librarymanagement.domain.book.domain.BookStatus;
import com.ys.librarymanagement.domain.book.domain.BookType;
import com.ys.librarymanagement.domain.book.exception.AlreadyRentedBookException;
import com.ys.librarymanagement.domain.book.exception.DuplicateBookException;
import com.ys.librarymanagement.domain.book.service.BookService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookControllerV1.class)
class BookControllerV1SliceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @DisplayName("post /api/v1/posts - 201 - 책 생성에 성공한다")
    @Test
    void createSuccess201() throws Exception {
        //given
        String bookName = "자바 프로그래밍";
        BookType bookType = BookType.COMPUTER;
        BookCreateRequest request = new BookCreateRequest(bookName, bookType);
        BookCreateResponse response = new BookCreateResponse(0L, bookName, bookType,
            BookStatus.RENTAL_AVAILABLE);

        given(bookService.createBookAndGetResponse(request))
            .willReturn(response);

        //when & then
        mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.bookId").exists())
            .andExpect(jsonPath("$.bookName").value(bookName))
            .andExpect(jsonPath("$.bookType").value(bookType.name()))
            .andExpect(jsonPath("$.bookStatus").value(BookStatus.RENTAL_AVAILABLE.name()))
            .andDo(print());

        verify(bookService).createBookAndGetResponse(request);
    }

    @DisplayName("post /api/v1/posts - 409 - 책이 이미 등록되어있으므로 생성에 실패한다")
    @Test
    void createFail201() throws Exception {
        //given
        String bookName = "자바 프로그래밍";
        BookType bookType = BookType.COMPUTER;
        BookCreateRequest request = new BookCreateRequest(bookName, bookType);

        given(bookService.createBookAndGetResponse(request))
            .willThrow(DuplicateBookException.class);

        //when & then
        mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
            )
            .andExpect(status().isConflict())
            .andDo(print());

        verify(bookService).createBookAndGetResponse(request);
    }


    @DisplayName("get /api/v1/books - 200 - 저장된 모든 책을 조회한다")
    @Test
    void findAllSuccess200() throws Exception {
        //given
        int size = 10;

        List<BookResponse> bookResponses = createBookResponses(size);

        given(bookService.findAllBooks())
            .willReturn(bookResponses);

        //when & then
        mockMvc.perform(get("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(size));

        verify(bookService).findAllBooks();
    }

    @DisplayName("get /api/v1/books - 404 - 저장된 책이 없으므로 notfound 응답을 보내준다")
    @Test
    void findAllFail404() throws Exception {
        //given
        given(bookService.findAllBooks())
            .willThrow(EntityNotFoundException.class);

        //when & then
        mockMvc.perform(get("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(bookService).findAllBooks();
    }
    
    @DisplayName("patch /api/v1/books/{bookId}/rental - 200 - 책 대여에 성공한다.")
    @Test
    void toRentalBookSuccess200() throws Exception {
        //given
        Long bookId = 0L;
        Long userId = 0L;
        Long bookRentalId = 0L;
        BookRentalRequest bookRentalRequest = new BookRentalRequest(userId);
        BookRentalResponse bookRentalResponse = new BookRentalResponse(bookRentalId);

        given(bookService.rentalBook(bookId, userId))
            .willReturn(bookRentalResponse);

        //when & then
        mockMvc.perform(patch("/api/v1/books/{bookId}/rental", bookId)
                .content(objectMapper.writeValueAsBytes(bookRentalRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bookRentalHistoryId").value(bookRentalId))
            .andDo(print());

        verify(bookService).rentalBook(bookId, userId);
    }

    @DisplayName("patch /api/v1/books/{bookId}/retnal - 404 - 유저나 책이 없으므로 책 대여에 실패한다.")
    @Test
    void toRentalBookSuccess404UserAndBookNotFound() throws Exception {
        //given
        Long bookId = 0L;
        Long userId = 0L;
        BookRentalRequest bookRentalRequest = new BookRentalRequest(userId);

        given(bookService.rentalBook(bookId, userId))
            .willThrow(EntityNotFoundException.class);

        //when & then
        mockMvc.perform(patch("/api/v1/books/{bookId}/rental", bookId)
                .content(objectMapper.writeValueAsBytes(bookRentalRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andDo(print());

        verify(bookService).rentalBook(bookId, userId);
    }

    @DisplayName("patch /api/v1/books/{bookId}/rental - 400 - 이미 대여된 책이므로 책 대여에 실패한다.")
    @Test
    void toRentalBookFail400AlreadyRented() throws Exception {
        //given
        Long bookId = 0L;
        Long userId = 0L;
        BookRentalRequest bookRentalRequest = new BookRentalRequest(userId);

        given(bookService.rentalBook(bookId, userId))
            .willThrow(AlreadyRentedBookException.class);

        //when & then
        mockMvc.perform(patch("/api/v1/books/{bookId}/rental", bookId)
                .content(objectMapper.writeValueAsBytes(bookRentalRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print());

        verify(bookService).rentalBook(bookId, userId);
    }

    @DisplayName("patch /api/v1/books/{bookId}/return - 200 - 책 반납에 성공한다.")
    @Test
    void toReturnBookSuccess() throws Exception {
        //given
        Long bookId = 0L;
        Long userId = 0L;
        BookReturnRequest bookReturnRequest = new BookReturnRequest(userId);

        willDoNothing().given(bookService).returnBook(bookId, userId);

        //when & then
        mockMvc.perform(patch("/api/v1/books/{bookId}/return", bookId)
                .content(objectMapper.writeValueAsBytes(bookReturnRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());

        verify(bookService).returnBook(bookId, userId);
    }

    @DisplayName("patch /api/v1/books/{bookId}/return - 400 - 책이 대여 상태가 아니므로 책 반납에 실패한다.")
    @Test
    void toReturnBookFailNotRentedBook() throws Exception {
        //given
        Long bookId = 0L;
        Long userId = 0L;
        BookReturnRequest bookReturnRequest = new BookReturnRequest(userId);

        willThrow(IllegalStateException.class).given(bookService).returnBook(bookId, userId);

        //when & then
        mockMvc.perform(patch("/api/v1/books/{bookId}/return", bookId)
                .content(objectMapper.writeValueAsBytes(bookReturnRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print());

        verify(bookService).returnBook(bookId, userId);
    }

    @DisplayName("patch /api/v1/books/{bookId}/return - 404 - 유저가 없으므로 책 반납에 실패한다.")
    @Test
    void toReturnBookFailNotFoundUser() throws Exception {
        //given
        Long bookId = 0L;
        Long userId = 0L;
        BookReturnRequest bookReturnRequest = new BookReturnRequest(userId);

        willThrow(EntityNotFoundException.class).given(bookService).returnBook(bookId, userId);

        //when & then
        mockMvc.perform(patch("/api/v1/books/{bookId}/return", bookId)
                .content(objectMapper.writeValueAsBytes(bookReturnRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andDo(print());

        verify(bookService).returnBook(bookId, userId);
    }

    @DisplayName("patch /api/v1/books/{bookId}/return - 404 - 책이 없으므로 책 반납에 실패한다.")
    @Test
    void toReturnBookFailNotFoundBook() throws Exception {
        //given
        Long bookId = 0L;
        Long userId = 0L;
        BookReturnRequest bookReturnRequest = new BookReturnRequest(userId);

        willThrow(EntityNotFoundException.class).given(bookService).returnBook(bookId, userId);

        //when & then
        mockMvc.perform(patch("/api/v1/books/{bookId}/return", bookId)
                .content(objectMapper.writeValueAsBytes(bookReturnRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andDo(print());

        verify(bookService).returnBook(bookId, userId);
    }

    @DisplayName("patch /api/v1/books/{bookId}/return - 403 - 유저의 책이 아니므로 책 반납에 실패한다.")
    @Test
    void toReturnBookFailNotUserRentedBook() throws Exception {
        //given
        Long bookId = 0L;
        Long userId = 0L;
        BookReturnRequest bookReturnRequest = new BookReturnRequest(userId);

        willThrow(NotUserRentedBookException.class).given(bookService).returnBook(bookId, userId);

        //when & then
        mockMvc.perform(patch("/api/v1/books/{bookId}/return", bookId)
                .content(objectMapper.writeValueAsBytes(bookReturnRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(bookService).returnBook(bookId, userId);
    }

    private List<BookResponse> createBookResponses(int size) {
        String bookName = "bookName";
        BookType bookType = BookType.COMPUTER;

        return IntStream.range(0, size)
            .mapToObj(value -> {
                Book book = new Book(bookName + value, bookType);

                return BookResponse.of(book);
            }).collect(Collectors.toList());
    }
}