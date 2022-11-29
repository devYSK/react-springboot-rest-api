package com.ys.librarymanagement.book.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.librarymanagement.book.domain.Book;
import com.ys.librarymanagement.book.domain.BookStatus;
import com.ys.librarymanagement.book.domain.BookType;
import com.ys.librarymanagement.book.exception.DuplicateBookException;
import com.ys.librarymanagement.book.service.BookService;
import com.ys.librarymanagement.common.exception.DuplicateException;
import com.ys.librarymanagement.common.exception.EntityNotFoundException;
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