package com.rookies6.myspringboot4project.repository;

import com.rookies6.myspringboot4project.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository
        extends JpaRepository<Book, Long> {

    // 기존 테스트에서 사용
    Optional<Book> findByIsbn(String isbn);

    // 기존 테스트에서 사용
    List<Book> findByAuthor(String author);

    // ISBN 중복 확인
    boolean existsByIsbn(String isbn);


    // ID + BookDetail 함께 조회
    @Query("""
        SELECT b
        FROM Book b
        LEFT JOIN FETCH b.bookDetail
        WHERE b.id = :id
    """)
    Optional<Book> findByIdWithBookDetail(
            @Param("id") Long id
    );


    // ISBN + BookDetail 함께 조회
    @Query("""
        SELECT b
        FROM Book b
        LEFT JOIN FETCH b.bookDetail
        WHERE b.isbn = :isbn
    """)
    Optional<Book> findByIsbnWithBookDetail(
            @Param("isbn") String isbn
    );


    // 저자 검색 + BookDetail 함께 조회
    @Query("""
        SELECT b
        FROM Book b
        LEFT JOIN FETCH b.bookDetail
        WHERE b.author LIKE %:author%
    """)
    List<Book> findByAuthorWithBookDetail(
            @Param("author") String author
    );


    // 제목 검색 + BookDetail 함께 조회
    @Query("""
        SELECT b
        FROM Book b
        LEFT JOIN FETCH b.bookDetail
        WHERE b.title LIKE %:title%
    """)
    List<Book> findByTitleWithBookDetail(
            @Param("title") String title
    );


    // 전체 조회 + BookDetail 함께 조회
    @Query("""
        SELECT b
        FROM Book b
        LEFT JOIN FETCH b.bookDetail
    """)
    List<Book> findAllWithBookDetail();
}
