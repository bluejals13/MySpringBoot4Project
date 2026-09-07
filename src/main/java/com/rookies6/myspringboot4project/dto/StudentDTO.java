package com.rookies6.myspringboot4project.dto;

import com.rookies6.myspringboot4project.entity.Student;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class StudentDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @NotBlank(message = "이름은 필수입니다.")
        @Size(max = 100, message = "이름은 100자를 초과할 수 없습니다.")
        private String name;

        @NotBlank(message = "학번은 필수입니다.")
        @Size(max = 20, message = "학번은 20자를 초과할 수 없습니다.")
        private String studentNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        private Long id;
        private String name;
        private String studentNumber;

        public static Response fromEntity(Student student) {
            return Response.builder()
                    .id(student.getId())
                    .name(student.getName())
                    .studentNumber(student.getStudentNumber())
                    .build();
        }
    }
}
