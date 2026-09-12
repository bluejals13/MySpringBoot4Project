package com.rookies6.myspringboot4project.repository;

import com.rookies6.myspringboot4project.entity.Publisher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PublisherRepository
        extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(
            String name,
            Long id
    );

    @Query("""
        SELECT DISTINCT p
        FROM Publisher p
        LEFT JOIN FETCH p.books
        """)
    List<Publisher> findAllWithBooks();

    @Query("""
        SELECT DISTINCT p
        FROM Publisher p
        LEFT JOIN FETCH p.books
        WHERE p.id = :id
        """)
    Optional<Publisher> findByIdWithBooks(
            @Param("id") Long id
    );
}
