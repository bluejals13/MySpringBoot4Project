package com.rookies6.myspringboot4project.service;

import com.rookies6.myspringboot4project.dto.BookDTO;
import com.rookies6.myspringboot4project.dto.PublisherDTO;
import com.rookies6.myspringboot4project.entity.Publisher;
import com.rookies6.myspringboot4project.exception.BusinessException;
import com.rookies6.myspringboot4project.exception.ErrorCode;
import com.rookies6.myspringboot4project.repository.PublisherRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PublisherService {

    private final PublisherRepository publisherRepository;


    // =========================
    // 전체 조회
    // =========================
    @Transactional(readOnly = true)
    public List<PublisherDTO.SimpleResponse> findAll() {

        return publisherRepository.findAllWithBooks()
                .stream()
                .map(publisher ->
                        PublisherDTO.SimpleResponse
                                .fromEntityWithCount(
                                        publisher,
                                        (long) publisher.getBooks().size()
                                )
                )
                .toList();
    }


    // =========================
    // ID 조회
    // =========================
    @Transactional(readOnly = true)
    public PublisherDTO.Response findById(Long id) {

        Publisher publisher =
                publisherRepository.findByIdWithBooks(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Publisher",
                                        "id",
                                        id
                                )
                        );

        return PublisherDTO.Response.fromEntity(publisher);
    }


    // =========================
    // 이름 조회
    // =========================
    @Transactional(readOnly = true)
    public PublisherDTO.Response findByName(
            String name
    ) {

        Publisher publisher =
                publisherRepository.findByName(name)
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Publisher",
                                        "name",
                                        name
                                )
                        );

        return PublisherDTO.Response.fromEntity(publisher);
    }


    // =========================
    // Publisher의 Book 조회
    // =========================
    @Transactional(readOnly = true)
    public List<BookDTO.SimpleResponse> findBooks(
            Long id
    ) {

        Publisher publisher =
                publisherRepository.findByIdWithBooks(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Publisher",
                                        "id",
                                        id
                                )
                        );

        return publisher.getBooks()
                .stream()
                .map(BookDTO.SimpleResponse::fromEntity)
                .toList();
    }


    // =========================
    // Publisher 생성
    // =========================
    public PublisherDTO.Response create(
            PublisherDTO.Request request
    ) {

        // 이름 중복 확인
        if (publisherRepository.existsByName(
                request.getName()
        )) {
            throw new BusinessException(
                    ErrorCode.PUBLISHER_NAME_DUPLICATE,
                    request.getName()
            );
        }

        Publisher publisher = Publisher.builder()
                .name(request.getName())
                .establishedDate(
                        request.getEstablishedDate()
                )
                .address(request.getAddress())
                .build();

        Publisher saved =
                publisherRepository.save(publisher);

        return PublisherDTO.Response.fromEntity(saved);
    }


    // =========================
    // Publisher 수정
    // =========================
    public PublisherDTO.Response update(
            Long id,
            PublisherDTO.Request request
    ) {

        Publisher publisher =
                publisherRepository.findByIdWithBooks(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Publisher",
                                        "id",
                                        id
                                )
                        );

        // 자기 자신을 제외한 이름 중복 확인
        if (publisherRepository.existsByNameAndIdNot(
                request.getName(),
                id
        )) {
            throw new BusinessException(
                    ErrorCode.PUBLISHER_NAME_DUPLICATE,
                    request.getName()
            );
        }

        publisher.setName(
                request.getName()
        );

        publisher.setEstablishedDate(
                request.getEstablishedDate()
        );

        publisher.setAddress(
                request.getAddress()
        );

        return PublisherDTO.Response.fromEntity(publisher);
    }


    // =========================
    // Publisher 삭제
    // =========================
    public void delete(Long id) {

        Publisher publisher =
                publisherRepository.findByIdWithBooks(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Publisher",
                                        "id",
                                        id
                                )
                        );

        // Book이 존재하면 삭제 불가
        if (!publisher.getBooks().isEmpty()) {
            throw new BusinessException(
                    ErrorCode.PUBLISHER_HAS_BOOKS,
                    id,
                    publisher.getBooks().size()
            );
        }

        publisherRepository.delete(publisher);
    }
}
