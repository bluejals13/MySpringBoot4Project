package com.rookies6.myspringboot4project.controller;

import com.rookies6.myspringboot4project.dto.BookDTO;
import com.rookies6.myspringboot4project.dto.PublisherDTO;
import com.rookies6.myspringboot4project.service.PublisherService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<PublisherDTO.SimpleResponse>> findAll() {

        return ResponseEntity.ok(
                publisherService.findAll()
        );
    }

    // ID 조회
    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO.Response> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                publisherService.findById(id)
        );
    }

    // 이름 조회
    @GetMapping("/name/{name}")
    public ResponseEntity<PublisherDTO.Response> findByName(
            @PathVariable String name
    ) {

        return ResponseEntity.ok(
                publisherService.findByName(name)
        );
    }

    // Publisher의 Book 목록
    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookDTO.SimpleResponse>> findBooks(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                publisherService.findBooks(id)
        );
    }

    // 생성
    @PostMapping
    public ResponseEntity<PublisherDTO.Response> create(
            @Valid @RequestBody PublisherDTO.Request request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(publisherService.create(request));
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<PublisherDTO.Response> update(
            @PathVariable Long id,
            @Valid @RequestBody PublisherDTO.Request request
    ) {

        return ResponseEntity.ok(
                publisherService.update(id, request)
        );
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        publisherService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
