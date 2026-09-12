package com.rookies6.myspringboot4project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private LocalDate establishedDate;

    private String address;

    @OneToMany(
            mappedBy = "publisher",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Book> books = new ArrayList<>();


    public void addBook(Book book) {

        books.add(book);

        book.changePublisher(this);
    }


    public void removeBook(Book book) {

        books.remove(book);

        if (book.getPublisher() == this) {
            book.changePublisher(null);
        }
    }
}
