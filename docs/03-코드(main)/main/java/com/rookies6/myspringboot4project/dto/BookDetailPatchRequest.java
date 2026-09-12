package com.rookies6.myspringboot4project.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDetailPatchRequest {

    private String description;

    private String language;

    private Integer pageCount;

    private String publisher;

    private String coverImageUrl;

    private String edition;
}
