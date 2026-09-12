package com.rookies6.myspringboot4project.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatchRequest {

    private String title;

    private String author;

    private String isbn;

    private Integer price;

    private LocalDate publishDate;

    private BookDTO.BookDetailDTO detailRequest;
}
