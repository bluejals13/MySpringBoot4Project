package com.rookies6.myspringboot4project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private Integer price;

    private LocalDate publishDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @OneToOne(
            mappedBy = "book",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private BookDetail bookDetail;


    public void changePublisher(Publisher publisher) {
        this.publisher = publisher;
    }


    public void setBookDetail(BookDetail bookDetail) {

        this.bookDetail = bookDetail;

        if (bookDetail != null && bookDetail.getBook() != this) {
            bookDetail.setBook(this);
        }
    }
}
