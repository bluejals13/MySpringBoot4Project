package com.rookies6.myspringboot4project.dto;

import com.rookies6.myspringboot4project.entity.Book;
import com.rookies6.myspringboot4project.entity.BookDetail;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

import java.time.LocalDate;

public class BookDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 100)
        private String title;

        @NotBlank(message = "저자는 필수입니다.")
        @Size(max = 20)
        private String author;

        @NotBlank(message = "ISBN은 필수입니다.")
        @Size(max = 20)
        private String isbn;

        @NotNull(message = "가격은 필수입니다.")
        @Min(value = 0, message = "가격은 음수일 수 없습니다.")
        private Integer price;

        @NotNull(message = "출판일은 필수입니다.")
        private LocalDate publishDate;

        @Valid
        private BookDetailDTO detailRequest;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailDTO {

        private String description;

        private String language;

        private Integer pageCount;

        private String publisher;

        private String coverImageUrl;

        private String edition;
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

        private BookDetailResponse detailResponse;

        public static Response fromEntity(Book book) {

            return Response.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .detailResponse(
                        book.getBookDetail() != null
                            ? BookDetailResponse.fromEntity(book.getBookDetail())
                            : null
                    )
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailResponse {

        private Long id;
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;

        public static BookDetailResponse fromEntity(BookDetail detail) {

            return BookDetailResponse.builder()
                    .id(detail.getId())
                    .description(detail.getDescription())
                    .language(detail.getLanguage())
                    .pageCount(detail.getPageCount())
                    .publisher(detail.getPublisher())
                    .coverImageUrl(detail.getCoverImageUrl())
                    .edition(detail.getEdition())
                    .build();
        }
    }
}
