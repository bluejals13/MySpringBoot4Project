package com.rookies6.myspringboot4project.entity;

import jakarta.persistence.*;
import lombok.*;

//Student
@Entity
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String studentNumber;

    // 연관관계의 주인(Owner)이 StudentDetail이므로 mappedBy에 studentDetail의 필드명(student)
    //을 지정한다.
    // 즉, FK(student_id)는 studentDetail 테이블에 저장된다.
    //
    // cascade = CascadeType.ALL:
    // student가 저장/수정/삭제될 때 studentDetail도 함께 처리된다.
    //
    // orphanRemoval = true:
    // student가 삭제되면 연결된 studentDetail도 자동으로 삭제된다.
    @OneToOne(fetch = FetchType.LAZY,
            mappedBy = "student",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private StudentDetail studentDetail;
}