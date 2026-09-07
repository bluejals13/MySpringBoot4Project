
package com.rookies6.myspringboot4project.service;

import com.rookies6.myspringboot4project.controller.dto.StudentDTO;
import com.rookies6.myspringboot4project.entity.Student;
import com.rookies6.myspringboot4project.exception.BusinessException;
import com.rookies6.myspringboot4project.exception.ErrorCode;
import com.rookies6.myspringboot4project.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;


    // =========================================================
    // 기존 Student CRUD
    // =========================================================

    /**
     * 전체 학생 조회
     */
    public List<StudentDTO.Response> getAllStudents() {

        return studentRepository.findAll()
                .stream()
                .map(StudentDTO.Response::fromEntity)
                .toList();
    }


    /**
     * ID로 학생 조회
     */
    public StudentDTO.Response getStudentById(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Student",
                        "id",
                        id
                ));

        return StudentDTO.Response.fromEntity(student);
    }


    /**
     * 학번으로 학생 조회
     */
    public StudentDTO.Response getStudentByStudentNumber(
            String studentNumber) {

        Student student = studentRepository
                .findByStudentNumber(studentNumber)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Student",
                        "student number",
                        studentNumber
                ));

        return StudentDTO.Response.fromEntity(student);
    }


    /**
     * 학생 생성
     */
    @Transactional
    public StudentDTO.Response createStudent(
            StudentDTO.Request request) {

        if (studentRepository.existsByStudentNumber(
                request.getStudentNumber())) {

            throw new BusinessException(
                    ErrorCode.STUDENT_NUMBER_DUPLICATE,
                    request.getStudentNumber()
            );
        }

        Student student = Student.builder()
                .name(request.getName())
                .studentNumber(request.getStudentNumber())
                .build();

        Student savedStudent = studentRepository.save(student);

        return StudentDTO.Response.fromEntity(savedStudent);
    }


    /**
     * 학생 수정
     */
    @Transactional
    public StudentDTO.Response updateStudent(
            Long id,
            StudentDTO.Request request) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Student",
                        "id",
                        id
                ));

        if (!student.getStudentNumber().equals(
                request.getStudentNumber())
                &&
                studentRepository.existsByStudentNumber(
                        request.getStudentNumber())) {

            throw new BusinessException(
                    ErrorCode.STUDENT_NUMBER_DUPLICATE,
                    request.getStudentNumber()
            );
        }

        student.setName(request.getName());
        student.setStudentNumber(request.getStudentNumber());

        return StudentDTO.Response.fromEntity(student);
    }


    /**
     * 학생 삭제
     */
    @Transactional
    public void deleteStudent(Long id) {

        if (!studentRepository.existsById(id)) {

            throw new BusinessException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "Student",
                    "id",
                    id
            );
        }

        studentRepository.deleteById(id);
    }


    // =========================================================
    // JPA Query Lab
    // =========================================================

    /**
     * ① 기본 findById()
     *
     * Student만 기본 조회한다.
     *
     * studentDetail은 LAZY이므로
     * 이후 접근할 때 추가 SELECT가 발생할 수 있다.
     */
    public Student getStudentForBasic(Long id) {

        return studentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Student",
                        "id",
                        id
                ));
    }


    /**
     * ② JOIN FETCH
     *
     * Student와 StudentDetail을
     * INNER JOIN FETCH로 함께 조회한다.
     */
    public Student getStudentUsingJoinFetch(Long id) {

        return studentRepository
                .findByIdWithJoinFetch(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Student",
                        "id",
                        id
                ));
    }


    /**
     * ③ LEFT JOIN FETCH
     *
     * StudentDetail이 없어도
     * Student는 조회한다.
     */
    public Student getStudentUsingLeftJoinFetch(Long id) {

        return studentRepository
                .findByIdWithLeftJoinFetch(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Student",
                        "id",
                        id
                ));
    }


    /**
     * ④ findAll()
     *
     * N+1 문제를 관찰하기 위한 실습용 조회.
     */
    public List<Student> getStudentsUsingFindAll() {

        return studentRepository.findAll();
    }


    /**
     * ⑤ LEFT JOIN FETCH 전체 조회
     *
     * Student와 StudentDetail을
     * 한 번에 조회하여 N+1 문제를 개선한다.
     */
    public List<Student> getStudentsUsingLeftJoinFetch() {

        return studentRepository.findAllWithLeftJoinFetch();
    }

   

}