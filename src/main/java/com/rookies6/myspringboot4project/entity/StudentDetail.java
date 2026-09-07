package com.rookies6.myspringboot4project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

//StudentDetail 클래스
@Entity
@Table(name = "student_details")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
//Owner(주인) - FK(외래키)를 가진 쪽이 주인임
public class StudentDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_detail_id")
    private Long id;
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column
    private LocalDate dateOfBirth;

    // 연관관계의 주인(Owner)
    // FK(student_id)를 실제로 가지고 있으므로 이쪽에서 관계를 관리한다.
    //
    // @JoinColumn:
    // student_details.student_id → students.student_id
    //
    // unique = true:
    // 한 학생에게 하나의 상세정보만 연결되도록 한다.
    // 즉, DB에서도 1:1 관계를 보장한다.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", unique = true)
    private Student student;

}