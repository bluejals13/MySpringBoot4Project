package com.rookies6.myspringboot4project.runner;

import com.rookies6.myspringboot4project.entity.Student;
import com.rookies6.myspringboot4project.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class DataInitRunner implements CommandLineRunner {

    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");

        createStudents();

        log.info("Data initialization completed successfully");
    }

    private void createStudents() {
        log.info("Creating students...");

        List<Student> students = List.of(
                createStudent("Alice Johnson", "CS001"),
                createStudent("Bob Smith", "CS002"),
                createStudent("Charlie Brown", "EE001"),
                createStudent("Diana Wilson", "EE002"),
                createStudent("Edward Davis", "ME001"),
                createStudent("Fiona Garcia", "BA001"),
                createStudent("George Martinez", "BA002"),
                createStudent("Helen Lee", "CS003")
        );

        for (Student student : students) {
            if (!studentRepository.existsByStudentNumber(student.getStudentNumber())) {
                studentRepository.save(student);

                log.info(
                        "Created student: {} ({})",
                        student.getName(),
                        student.getStudentNumber()
                );
            } else {
                log.info(
                        "Student already exists: {} ({})",
                        student.getName(),
                        student.getStudentNumber()
                );
            }
        }
    }

    private Student createStudent(String name, String studentNumber) {
        return Student.builder()
                .name(name)
                .studentNumber(studentNumber)
                .build();
    }
}
