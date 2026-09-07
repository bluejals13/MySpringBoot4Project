package com.rookies6.myspringboot4project.service;

import com.rookies6.myspringboot4project.dto.BookDTO;
import com.rookies6.myspringboot4project.entity.Book;
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
        return bookRepository.findAll()
                .stream()
                .map(BookDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public BookDTO.Response getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 책을 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }

    public BookDTO.Response getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("해당 ISBN의 책을 찾을 수 없습니다: " + isbn, HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }

    // 저자는 여러 권의 책을 쓸 수 있으므로 List 반환
    public List<BookDTO.Response> getBookByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthor(author);
        if (books.isEmpty()) {
            throw new BusinessException("해당 저자의 책을 찾을 수 없습니다: " + author, HttpStatus.NOT_FOUND);
        }
        return books.stream().map(BookDTO.Response::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("이미 존재하는 ISBN입니다: " + request.getIsbn(), HttpStatus.CONFLICT);
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        Book savedBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(savedBook);
    }

    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 책을 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));

        if (!book.getIsbn().equals(request.getIsbn()) && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("이미 존재하는 ISBN입니다: " + request.getIsbn(), HttpStatus.CONFLICT);
        }

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());

        return BookDTO.Response.fromEntity(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BusinessException("해당 ID의 책을 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(id);
    }
}