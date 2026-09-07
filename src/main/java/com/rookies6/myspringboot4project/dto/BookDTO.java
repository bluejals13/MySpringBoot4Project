package com.rookies6.myspringboot4project.dto;

import com.rookies6.myspringboot4project.entity.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class BookDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 100, message = "제목은 100자를 초과할 수 없습니다.")
        private String title;

        @NotBlank(message = "저자는 필수입니다.")
        @Size(max = 20, message = "저자는 20자를 초과할 수 없습니다.")
        private String author;

        @NotBlank(message = "ISBN은 필수입니다.")
        @Size(max = 20, message = "ISBN은 20자를 초과할 수 없습니다.")
        private String isbn;

        @NotNull(message = "가격은 필수입니다.")
        private Integer price;

        @NotNull(message = "출판일은 필수입니다.")
        private LocalDate publishDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        private Long id;
        private String title;
        private String author;
        private String isbn;
        private int price;
        private LocalDate publishDate;

        public static Response fromEntity(Book book) {
            return Response.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .build();
        }
    }
}