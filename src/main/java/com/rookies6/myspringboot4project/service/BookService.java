package com.rookies6.myspringboot4project.service;

import com.rookies6.myspringboot4project.dto.BookDTO;
import com.rookies6.myspringboot4project.dto.BookDetailPatchRequest;
import com.rookies6.myspringboot4project.dto.PatchRequest;
import com.rookies6.myspringboot4project.entity.Book;
import com.rookies6.myspringboot4project.entity.BookDetail;
import com.rookies6.myspringboot4project.exception.BusinessException;
import com.rookies6.myspringboot4project.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAllWithBookDetail()
                .stream()
                .map(BookDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public BookDTO.Response getBookById(Long id) {

        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() ->
                        new BusinessException(
                                "해당 ID의 책을 찾을 수 없습니다: " + id,
                                HttpStatus.NOT_FOUND
                        )
                );

        return BookDTO.Response.fromEntity(book);
    }


    public BookDTO.Response getBookByIsbn(String isbn) {

        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() ->
                        new BusinessException(
                                "해당 ISBN의 책을 찾을 수 없습니다: " + isbn,
                                HttpStatus.NOT_FOUND
                        )
                );

        return BookDTO.Response.fromEntity(book);
    }


    // 저자는 여러 권의 책을 쓸 수 있으므로 List 반환
    public List<BookDTO.Response> getBookByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthorWithBookDetail(author);
        if (books.isEmpty()) {
            throw new BusinessException("해당 저자의 책을 찾을 수 없습니다: " + author, HttpStatus.NOT_FOUND);
        }
        return books.stream().map(BookDTO.Response::fromEntity).collect(Collectors.toList());
    }


    public List<BookDTO.Response> getBookByTitle(String title) {

        List<Book> books =
                bookRepository.findByTitleWithBookDetail(title);

        if (books.isEmpty()) {
            throw new BusinessException(
                    "해당 제목의 책을 찾을 수 없습니다: " + title,
                    HttpStatus.NOT_FOUND
            );
        }

        return books.stream()
                .map(BookDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }


    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {

        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(
                    "이미 존재하는 ISBN입니다: " + request.getIsbn(),
                    HttpStatus.CONFLICT
            );
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        if (request.getDetailRequest() != null) {

            BookDTO.BookDetailDTO detailRequest =
                    request.getDetailRequest();

            BookDetail detail = BookDetail.builder()
                    .description(detailRequest.getDescription())
                    .language(detailRequest.getLanguage())
                    .pageCount(detailRequest.getPageCount())
                    .publisher(detailRequest.getPublisher())
                    .coverImageUrl(detailRequest.getCoverImageUrl())
                    .edition(detailRequest.getEdition())
                    .book(book)
                    .build();

            book.setBookDetail(detail);
        }

        Book savedBook = bookRepository.save(book);

        return BookDTO.Response.fromEntity(savedBook);
    }


    @Transactional
    public BookDTO.Response updateBook(
            Long id,
            BookDTO.Request request) {

        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() ->
                        new BusinessException(
                                "해당 ID의 책을 찾을 수 없습니다: " + id,
                                HttpStatus.NOT_FOUND
                        )
                );

        // ISBN 변경 시 중복 체크
        if (!book.getIsbn().equals(request.getIsbn())
                && bookRepository.existsByIsbn(request.getIsbn())) {

            throw new BusinessException(
                    "이미 존재하는 ISBN입니다: " + request.getIsbn(),
                    HttpStatus.CONFLICT
            );
        }

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());

        if (request.getDetailRequest() != null) {

            BookDTO.BookDetailDTO dto =
                    request.getDetailRequest();

            BookDetail detail = book.getBookDetail();

            if (detail == null) {

                detail = BookDetail.builder()
                        .book(book)
                        .build();

                book.setBookDetail(detail);
            }

            detail.setDescription(dto.getDescription());
            detail.setLanguage(dto.getLanguage());
            detail.setPageCount(dto.getPageCount());
            detail.setPublisher(dto.getPublisher());
            detail.setCoverImageUrl(dto.getCoverImageUrl());
            detail.setEdition(dto.getEdition());
        }

        return BookDTO.Response.fromEntity(book);
    }


    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BusinessException("해당 ID의 책을 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(id);
    }

    @Transactional
    public BookDTO.Response patchBook(
            Long id,
            PatchRequest request) {

        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() ->
                        new BusinessException(
                                "해당 ID의 책을 찾을 수 없습니다: " + id,
                                HttpStatus.NOT_FOUND
                        )
                );

        // 제목
        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }

        // 저자
        if (request.getAuthor() != null) {
            book.setAuthor(request.getAuthor());
        }

        // ISBN
        if (request.getIsbn() != null) {

            if (!book.getIsbn().equals(request.getIsbn())
                    && bookRepository.existsByIsbn(request.getIsbn())) {

                throw new BusinessException(
                        "이미 존재하는 ISBN입니다: " + request.getIsbn(),
                        HttpStatus.CONFLICT
                );
            }

            book.setIsbn(request.getIsbn());
        }

        // 가격
        if (request.getPrice() != null) {
            book.setPrice(request.getPrice());
        }

        // 출판일
        if (request.getPublishDate() != null) {
            book.setPublishDate(request.getPublishDate());
        }

        // BookDetail 부분 수정
        if (request.getDetailRequest() != null) {

            BookDTO.BookDetailDTO detailRequest =
                    request.getDetailRequest();

            BookDetail detail = book.getBookDetail();

            if (detail == null) {

                detail = BookDetail.builder()
                        .book(book)
                        .build();

                book.setBookDetail(detail);
            }

            if (detailRequest.getDescription() != null) {
                detail.setDescription(
                        detailRequest.getDescription()
                );
            }

            if (detailRequest.getLanguage() != null) {
                detail.setLanguage(
                        detailRequest.getLanguage()
                );
            }

            if (detailRequest.getPageCount() != null) {
                detail.setPageCount(
                        detailRequest.getPageCount()
                );
            }

            if (detailRequest.getPublisher() != null) {
                detail.setPublisher(
                        detailRequest.getPublisher()
                );
            }

            if (detailRequest.getCoverImageUrl() != null) {
                detail.setCoverImageUrl(
                        detailRequest.getCoverImageUrl()
                );
            }

            if (detailRequest.getEdition() != null) {
                detail.setEdition(
                        detailRequest.getEdition()
                );
            }
        }

        return BookDTO.Response.fromEntity(book);
    }


    @Transactional
    public BookDTO.Response patchBookDetail(
            Long id,
            BookDetailPatchRequest request) {

        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() ->
                        new BusinessException(
                                "해당 ID의 책을 찾을 수 없습니다: " + id,
                                HttpStatus.NOT_FOUND
                        )
                );

        BookDetail detail = book.getBookDetail();

        if (detail == null) {
            detail = BookDetail.builder()
                    .book(book)
                    .build();

            book.setBookDetail(detail);
        }

        if (request.getDescription() != null) {
            detail.setDescription(request.getDescription());
        }

        if (request.getLanguage() != null) {
            detail.setLanguage(request.getLanguage());
        }

        if (request.getPageCount() != null) {
            detail.setPageCount(request.getPageCount());
        }

        if (request.getPublisher() != null) {
            detail.setPublisher(request.getPublisher());
        }

        if (request.getCoverImageUrl() != null) {
            detail.setCoverImageUrl(request.getCoverImageUrl());
        }

        if (request.getEdition() != null) {
            detail.setEdition(request.getEdition());
        }

        return BookDTO.Response.fromEntity(book);
    }



}