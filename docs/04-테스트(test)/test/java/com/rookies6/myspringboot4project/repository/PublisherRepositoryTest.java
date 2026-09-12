package com.rookies6.myspringboot4project.repository;

import com.rookies6.myspringboot4project.entity.Publisher;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PublisherRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PublisherRepository publisherRepository;

    private Publisher publisher;

    @BeforeEach
    void setUp() {

        publisher = Publisher.builder()
                .name("Penguin Random House")
                .establishedDate(LocalDate.of(2013, 7, 1))
                .address("1745 Broadway, New York, NY")
                .build();

        entityManager.persist(publisher);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByName_ShouldReturnPublisher() {

        Optional<Publisher> found =
                publisherRepository.findByName(
                        "Penguin Random House"
                );

        assertThat(found).isPresent();
        assertThat(found.get().getName())
                .isEqualTo("Penguin Random House");

        assertThat(found.get().getEstablishedDate())
                .isEqualTo(LocalDate.of(2013, 7, 1));

        assertThat(found.get().getAddress())
                .isEqualTo("1745 Broadway, New York, NY");
    }

    @Test
    void findByName_ShouldReturnEmpty_WhenNotFound() {

        Optional<Publisher> found =
                publisherRepository.findByName(
                        "Non-Existent Publisher"
                );

        assertThat(found).isEmpty();
    }

    @Test
    void existsByName_ShouldReturnTrue() {

        boolean exists =
                publisherRepository.existsByName(
                        "Penguin Random House"
                );

        assertThat(exists).isTrue();
    }

    @Test
    void existsByName_ShouldReturnFalse() {

        boolean exists =
                publisherRepository.existsByName(
                        "Non-Existent Publisher"
                );

        assertThat(exists).isFalse();
    }

    @Test
    void findByIdWithBooks_ShouldReturnPublisher() {

        Optional<Publisher> found =
                publisherRepository.findByIdWithBooks(
                        publisher.getId()
                );

        assertThat(found).isPresent();

        assertThat(found.get().getName())
                .isEqualTo("Penguin Random House");

        assertThat(found.get().getBooks())
                .isNotNull();
    }
}
