package com.rookies6.myspringboot4project.repository;

import com.rookies6.myspringboot4project.entity.BookDetail;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookDetailRepository
        extends JpaRepository<BookDetail, Long> {

    Optional<BookDetail> findByBookId(Long bookId);

    @Query("""
        SELECT bd
        FROM BookDetail bd
        JOIN FETCH bd.book
        WHERE bd.id = :id
    """)
    Optional<BookDetail> findByIdWithBook(@Param("id") Long id);

    List<BookDetail> findByPublisher(String publisher);
}
