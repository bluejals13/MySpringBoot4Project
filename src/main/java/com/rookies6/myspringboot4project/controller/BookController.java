package com.rookies6.myspringboot4project.controller;

import com.rookies6.myspringboot4project.dto.BookDTO;
import com.rookies6.myspringboot4project.dto.BookDetailDTO;

import com.rookies6.myspringboot4project.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;


    // =========================
    // 생성
    // =========================
    @PostMapping
    public ResponseEntity<BookDTO.Response> createBook(
            @Valid @RequestBody BookDTO.Request request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.createBook(request));
    }


    // =========================
    // 전체 조회
    // =========================
    @GetMapping
    public ResponseEntity<List<BookDTO.Response>> getAllBooks() {

        return ResponseEntity.ok(
                bookService.getAllBooks()
        );
    }


    // =========================
    // ID 조회
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.Response> getBookById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                bookService.getBookById(id)
        );
    }


    // =========================
    // ISBN 조회
    // =========================
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.Response> getBookByIsbn(
            @PathVariable String isbn
    ) {

        return ResponseEntity.ok(
                bookService.getBookByIsbn(isbn)
        );
    }


    // =========================
    // 저자 검색
    // =========================
    @GetMapping("/search/author")
    public ResponseEntity<List<BookDTO.Response>> searchByAuthor(
            @RequestParam String author
    ) {

        return ResponseEntity.ok(
                bookService.getBooksByAuthor(author)
        );
    }


    // =========================
    // 제목 검색
    // =========================
    @GetMapping("/search/title")
    public ResponseEntity<List<BookDTO.Response>> searchByTitle(
            @RequestParam String title
    ) {

        return ResponseEntity.ok(
                bookService.getBooksByTitle(title)
        );
    }


    // =========================
    // 출판사별 도서 조회
    // =========================
    @GetMapping("/publisher/{publisherId}")
    public ResponseEntity<List<BookDTO.Response>> getBooksByPublisher(
            @PathVariable Long publisherId
    ) {

        return ResponseEntity.ok(
                bookService.getBooksByPublisherId(publisherId)
        );
    }


    // =========================
    // 수정
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.Response> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookDTO.Request request
    ) {

        return ResponseEntity.ok(
                bookService.updateBook(id, request)
        );
    }


        // =========================
        // 부분 수정
        // =========================
        @PatchMapping("/{id}")
        public ResponseEntity<BookDTO.Response> patchBook(
                @PathVariable Long id,
                @Valid @RequestBody BookDTO.PatchRequest request
        ) {
        return ResponseEntity.ok(
                bookService.patchBook(id, request)
        );
        }


        // =========================
        // BookDetail 부분 수정
        // =========================
        @PatchMapping("/{id}/detail")
        public ResponseEntity<BookDTO.Response> patchBookDetail(
                @PathVariable Long id,
                @Valid @RequestBody BookDetailDTO.PatchRequest request
        ) {
        return ResponseEntity.ok(
                bookService.patchBookDetail(id, request)
        );
        }




    // =========================
    // 삭제
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id
    ) {

        bookService.deleteBook(id);

        return ResponseEntity.noContent().build();
    }
}
