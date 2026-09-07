package com.rookies6.myspringboot4project.repository;

import com.rookies6.myspringboot4project.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentNumber(String studentNumber);

    boolean existsByStudentNumber(String studentNumber);

    // =========================================
    // JPA Query Lab
    // =========================================

    // ② INNER JOIN FETCH
    @Query("""
            SELECT DISTINCT s
            FROM Student s
            JOIN FETCH s.studentDetail
            WHERE s.id = :id
            """)
    Optional<Student> findByIdWithJoinFetch(@Param("id") Long id);

    // ③ LEFT JOIN FETCH
    @Query("""
            SELECT DISTINCT s
            FROM Student s
            LEFT JOIN FETCH s.studentDetail
            WHERE s.id = :id
            """)
    Optional<Student> findByIdWithLeftJoinFetch(@Param("id") Long id);

    // ⑤ 전체 LEFT JOIN FETCH
    @Query("""
            SELECT DISTINCT s
            FROM Student s
            LEFT JOIN FETCH s.studentDetail
            """)
    List<Student> findAllWithLeftJoinFetch();
}
