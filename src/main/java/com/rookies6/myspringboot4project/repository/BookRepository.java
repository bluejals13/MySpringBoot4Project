package com.rookies6.myspringboot4project.repository;

import com.rookies6.myspringboot4project.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);
    
    boolean existsByIsbn(String isbn); // Service에서 중복 체크용으로 사용

    List<Book> findByAuthor(String author);
}