package com.rookies6.myspringboot4project.service;

import com.rookies6.myspringboot4project.dto.BookDTO;
import com.rookies6.myspringboot4project.dto.BookDetailDTO;
import com.rookies6.myspringboot4project.entity.Book;
import com.rookies6.myspringboot4project.entity.BookDetail;
import com.rookies6.myspringboot4project.entity.Publisher;
import com.rookies6.myspringboot4project.exception.BusinessException;
import com.rookies6.myspringboot4project.exception.ErrorCode;
import com.rookies6.myspringboot4project.repository.BookRepository;
import com.rookies6.myspringboot4project.repository.PublisherRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;


    // =========================
    // 책 생성
    // =========================
    public BookDTO.Response createBook(BookDTO.Request request) {

        // ISBN 중복 확인
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(
                    ErrorCode.ISBN_DUPLICATE,
                    request.getIsbn()
            );
        }

        // Publisher 존재 확인
        Publisher publisher = publisherRepository.findById(
                request.getPublisherId()
        ).orElseThrow(() ->
                new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Publisher",
                        "id",
                        request.getPublisherId()
                )
        );

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .publisher(publisher)
                .build();

        // =========================
        // BookDetail 생성
        // =========================
        if (request.getDetailRequest() != null) {

            BookDetailDTO detailRequest =
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

        Book saved = bookRepository.save(book);

        return BookDTO.Response.fromEntity(saved);
    }


    // =========================
    // 전체 조회
    // =========================
    @Transactional(readOnly = true)
    public List<BookDTO.Response> getAllBooks() {

        return bookRepository.findAllWithDetails()
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }


    // =========================
    // ID 조회
    // =========================
    @Transactional(readOnly = true)
    public BookDTO.Response getBookById(Long id) {

        Book book = bookRepository.findByIdWithAllDetails(id)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Book",
                                "id",
                                id
                        )
                );

        return BookDTO.Response.fromEntity(book);
    }


    // =========================
    // ISBN 조회
    // =========================
    @Transactional(readOnly = true)
    public BookDTO.Response getBookByIsbn(String isbn) {

        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Book",
                                "isbn",
                                isbn
                        )
                );

        return BookDTO.Response.fromEntity(book);
    }


    // =========================
    // 저자 검색
    // =========================
    @Transactional(readOnly = true)
    public List<BookDTO.Response> getBooksByAuthor(String author) {

        return bookRepository
                .findByAuthorContainingIgnoreCase(author)
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }


    // =========================
    // 제목 검색
    // =========================
    @Transactional(readOnly = true)
    public List<BookDTO.Response> getBooksByTitle(String title) {

        return bookRepository
                .findByTitleContainingIgnoreCase(title)
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }


    // =========================
    // Publisher별 Book 조회
    // =========================
    @Transactional(readOnly = true)
    public List<BookDTO.Response> getBooksByPublisherId(
            Long publisherId
    ) {

        // Publisher 존재 확인
        if (!publisherRepository.existsById(publisherId)) {
            throw new BusinessException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "Publisher",
                    "id",
                    publisherId
            );
        }

        return bookRepository.findByPublisherId(publisherId)
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }


    // =========================
    // 책 수정
    // =========================
    public BookDTO.Response updateBook(
            Long id,
            BookDTO.Request request
    ) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Book",
                                "id",
                                id
                        )
                );

        // ISBN 중복 확인
        if (bookRepository.existsByIsbnAndIdNot(
                request.getIsbn(),
                id
        )) {
            throw new BusinessException(
                    ErrorCode.ISBN_DUPLICATE,
                    request.getIsbn()
            );
        }

        // Publisher 존재 확인
        Publisher publisher = publisherRepository.findById(
                request.getPublisherId()
        ).orElseThrow(() ->
                new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Publisher",
                        "id",
                        request.getPublisherId()
                )
        );

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());

        book.changePublisher(publisher);

        return BookDTO.Response.fromEntity(book);
    }


    // =========================
    // 책 삭제
    // =========================
    public void deleteBook(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Book",
                                "id",
                                id
                        )
                );

        bookRepository.delete(book);
    }


    // =========================
    // Book PATCH
    // =========================
    public BookDTO.Response patchBook(
        Long id,
        BookDTO.PatchRequest request
        ) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Book",
                                "id",
                                id
                        )
                );

        if (request.getIsbn() != null) {

                if (bookRepository.existsByIsbnAndIdNot(
                        request.getIsbn(),
                        id
                )) {
                throw new BusinessException(
                        ErrorCode.ISBN_DUPLICATE,
                        request.getIsbn()
                );
                }

                book.setIsbn(request.getIsbn());
        }

        if (request.getTitle() != null) {
                book.setTitle(request.getTitle());
        }

        if (request.getAuthor() != null) {
                book.setAuthor(request.getAuthor());
        }

        if (request.getPrice() != null) {
                book.setPrice(request.getPrice());
        }

        if (request.getPublishDate() != null) {
                book.setPublishDate(request.getPublishDate());
        }

        if (request.getPublisherId() != null) {
                Publisher publisher = publisherRepository.findById(
                        request.getPublisherId()
                ).orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Publisher",
                                "id",
                                request.getPublisherId()
                        )
                );

                book.changePublisher(publisher);
        }

        return BookDTO.Response.fromEntity(book);
        }



    // =========================
    // BookDetail PATCH
    // =========================
        public BookDTO.Response patchBookDetail(
                Long id,
                BookDetailDTO.PatchRequest request
        ) {

        Book book = bookRepository.findByIdWithAllDetails(id)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Book",
                                "id",
                                id
                        )
                );

        BookDetail detail = book.getBookDetail();

        // Detail이 없으면 생성
        if (detail == null) {

            detail = BookDetail.builder()
                    .book(book)
                    .build();

            book.setBookDetail(detail);
        }

        if (request.getDescription() != null) {
            detail.setDescription(
                    request.getDescription()
            );
        }

        if (request.getLanguage() != null) {
            detail.setLanguage(
                    request.getLanguage()
            );
        }

        if (request.getPageCount() != null) {
            detail.setPageCount(
                    request.getPageCount()
            );
        }

        if (request.getPublisher() != null) {
            detail.setPublisher(
                    request.getPublisher()
            );
        }

        if (request.getCoverImageUrl() != null) {
            detail.setCoverImageUrl(
                    request.getCoverImageUrl()
            );
        }

        if (request.getEdition() != null) {
            detail.setEdition(
                    request.getEdition()
            );
        }

        return BookDTO.Response.fromEntity(book);
    }
}
