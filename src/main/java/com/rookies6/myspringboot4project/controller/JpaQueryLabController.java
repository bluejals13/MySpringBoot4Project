package com.rookies6.myspringboot4project.controller;

import com.rookies6.myspringboot4project.controller.dto.StudentDTO;
import com.rookies6.myspringboot4project.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/jpa-lab")
@RequiredArgsConstructor
public class JpaQueryLabController {

    private final StudentService studentService;

    // =========================================================
    // JPA Query Lab 화면
    // =========================================================

    @GetMapping("/page")
    public String jpaQueryLabPage() {
        return "jpa-query-lab";
    }


    // =========================================================
    // 1. 단건 조회
    // =========================================================

    // ① 기본 findById
    @GetMapping("/student/{id}/basic")
    @ResponseBody
    public StudentDTO.Response basic(
            @PathVariable Long id) {

        return StudentDTO.Response.fromEntity(
                studentService.getStudentForBasic(id)
        );
    }


    // ② JOIN FETCH
    @GetMapping("/student/{id}/join-fetch")
    @ResponseBody
    public StudentDTO.Response joinFetch(
            @PathVariable Long id) {

        return StudentDTO.Response.fromEntity(
                studentService.getStudentUsingJoinFetch(id)
        );
    }


    // ③ LEFT JOIN FETCH
    @GetMapping("/student/{id}/left-join-fetch")
    @ResponseBody
    public StudentDTO.Response leftJoinFetch(
            @PathVariable Long id) {

        return StudentDTO.Response.fromEntity(
                studentService.getStudentUsingLeftJoinFetch(id)
        );
    }


    // =========================================================
    // 2. 전체 조회
    // =========================================================

    // ④ findAll → N+1 관찰
    @GetMapping("/students/basic")
    @ResponseBody
    public List<StudentDTO.Response> basicList() {

        return studentService.getStudentsUsingFindAll()
                .stream()
                .map(StudentDTO.Response::fromEntity)
                .toList();
    }


    // ⑤ LEFT JOIN FETCH → N+1 개선
    @GetMapping("/students/left-join-fetch")
    @ResponseBody
    public List<StudentDTO.Response> leftJoinFetchList() {

        return studentService.getStudentsUsingLeftJoinFetch()
                .stream()
                .map(StudentDTO.Response::fromEntity)
                .toList();
    }
}
