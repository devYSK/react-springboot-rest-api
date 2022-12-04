package com.ys.librarymanagement.domain.book.api.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookNameRentalRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String bookName;

}
