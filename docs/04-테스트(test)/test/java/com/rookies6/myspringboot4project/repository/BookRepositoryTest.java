package com.rookies6.myspringboot4project.repository;

import com.rookies6.myspringboot4project.entity.Book;
import com.rookies6.myspringboot4project.entity.BookDetail;
import com.rookies6.myspringboot4project.entity.Publisher;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BookRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private Publisher publisher;
    private Book book;
    private BookDetail bookDetail;

    @BeforeEach
    void setUp() {

        publisher = Publisher.builder()
                .name("Penguin Random House")
                .establishedDate(LocalDate.of(2013, 7, 1))
                .address("1745 Broadway, New York, NY")
                .build();

        entityManager.persist(publisher);
        entityManager.flush();

        book = Book.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .price(45000)
                .publishDate(LocalDate.of(2008, 8, 1))
                .publisher(publisher)
                .build();

        entityManager.persist(book);
        entityManager.flush();

        bookDetail = BookDetail.builder()
                .description("A handbook of agile software craftsmanship")
                .language("English")
                .pageCount(464)
                .publisher("Prentice Hall")
                .edition("1st Edition")
                .book(book)
                .build();

        entityManager.persist(bookDetail);
        entityManager.flush();

        book.setBookDetail(bookDetail);
        entityManager.flush();

        entityManager.clear();
    }

    @Test
    void findByIsbn_ShouldReturnBook() {

        Optional<Book> found =
                bookRepository.findByIsbn("978-0132350884");

        assertThat(found).isPresent();
        assertThat(found.get().getTitle())
                .isEqualTo("Clean Code");
        assertThat(found.get().getAuthor())
                .isEqualTo("Robert C. Martin");
    }

    @Test
    void findByIsbn_ShouldReturnEmpty_WhenNotFound() {

        Optional<Book> found =
                bookRepository.findByIsbn("000-0000000000");

        assertThat(found).isEmpty();
    }

    @Test
    void findByIdWithAllDetails_ShouldReturnBookWithAllDetails() {

        Optional<Book> found =
                bookRepository.findByIdWithAllDetails(book.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getBookDetail()).isNotNull();
        assertThat(found.get().getPublisher()).isNotNull();

        assertThat(found.get().getPublisher().getName())
                .isEqualTo("Penguin Random House");
    }

    @Test
    void findByPublisherId_ShouldReturnBooks() {

        List<Book> found =
                bookRepository.findByPublisherId(publisher.getId());

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle())
                .isEqualTo("Clean Code");
    }

    @Test
    void countByPublisherId_ShouldReturnCorrectCount() {

        Long count =
                bookRepository.countByPublisherId(publisher.getId());

        assertThat(count).isEqualTo(1);
    }

    @Test
    void existsByIsbn_ShouldReturnTrue() {

        boolean exists =
                bookRepository.existsByIsbn("978-0132350884");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByIsbn_ShouldReturnFalse() {

        boolean exists =
                bookRepository.existsByIsbn("000-0000000000");

        assertThat(exists).isFalse();
    }

    @Test
    void findByAuthorContainingIgnoreCase_ShouldReturnBooks() {

        List<Book> found =
                bookRepository
                        .findByAuthorContainingIgnoreCase("martin");

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getAuthor())
                .contains("Martin");
    }

    @Test
    void findByTitleContainingIgnoreCase_ShouldReturnBooks() {

        List<Book> found =
                bookRepository
                        .findByTitleContainingIgnoreCase("clean");

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle())
                .contains("Clean");
    }
}
