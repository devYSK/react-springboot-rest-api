package com.ys.librarymanagement.book.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.Objects;

public enum BookType {

    COMPUTER,
    SCIENCE,
    ECONOMY,
    COMIC,
    SPORTS;

    @JsonCreator
    public static BookType of(String bookType) {
        return Arrays.stream(values())
            .filter(typeName -> Objects.equals(typeName.name(), bookType))
            .findFirst()
            .orElseThrow();
    }

}
