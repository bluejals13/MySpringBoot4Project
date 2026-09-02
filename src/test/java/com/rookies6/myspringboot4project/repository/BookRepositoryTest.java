package com.rookies6.myspringboot4project.repository;

import com.rookies6.myspringboot4project.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();

        Book book = Book.builder()
                .title("스프링 부트 입문")
                .author("홍길동")
                .isbn("9788956746425")
                .price(30000)
                .publishDate(LocalDate.of(2025, 5, 7))
                .build();

        bookRepository.save(book);
    }

    @Test
    void testCreateBook() {
        Book book = Book.builder()
                .title("JPA 프로그래밍")
                .author("김철수")
                .isbn("1234567890123")
                .price(35000)
                .publishDate(LocalDate.of(2025, 6, 1))
                .build();

        Book savedBook = bookRepository.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("JPA 프로그래밍");
        assertThat(savedBook.getAuthor()).isEqualTo("김철수");
        assertThat(savedBook.getIsbn()).isEqualTo("1234567890123");
        assertThat(savedBook.getPrice()).isEqualTo(35000);
        assertThat(savedBook.getPublishDate())
                .isEqualTo(LocalDate.of(2025, 6, 1));
    }

    @Test
    void testFindByIsbn() {
        Optional<Book> result =
                bookRepository.findByIsbn("9788956746425");

        assertThat(result).isPresent();
        assertThat(result.get().getTitle())
                .isEqualTo("스프링 부트 입문");
        assertThat(result.get().getAuthor())
                .isEqualTo("홍길동");
    }

    @Test
    void testFindByAuthor() {
        List<Book> books =
                bookRepository.findByAuthor("홍길동");

        assertThat(books).isNotEmpty();
        assertThat(books)
                .anyMatch(book ->
                        book.getTitle().equals("스프링 부트 입문"));
    }

    @Test
    void testUpdateBook() {
        Book book = bookRepository
                .findByIsbn("9788956746425")
                .orElseThrow();

        book.setPrice(32000);
        book.setTitle("스프링 부트 입문 개정판");

        Book updatedBook = bookRepository.save(book);

        assertThat(updatedBook.getPrice()).isEqualTo(32000);
        assertThat(updatedBook.getTitle())
                .isEqualTo("스프링 부트 입문 개정판");
    }

    @Test
    void testDeleteBook() {
        Book book = bookRepository
                .findByIsbn("9788956746425")
                .orElseThrow();

        Long id = book.getId();

        bookRepository.delete(book);

        Optional<Book> result =
                bookRepository.findById(id);

        assertThat(result).isEmpty();
    }
}
