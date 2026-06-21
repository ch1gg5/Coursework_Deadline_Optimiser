package com.chiggs.coursework_deadline_optimiser.service;

import com.chiggs.coursework_deadline_optimiser.dto.StudentRequest;
import com.chiggs.coursework_deadline_optimiser.model.Student;
import com.chiggs.coursework_deadline_optimiser.repo.StudentRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepo studentRepo;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void StudentService_CreateStudent_ReturnsStudentDto() {
        Student student = Student.builder()
                .name("John Doe")
                .email("John@gmail.com")
                .maxHoursPerDay(5).build();

        StudentRequest studentDto = StudentRequest.builder()
                .name("John Doe")
                .email("John@gmail.com")
                .maxHoursPerDay(5).build();

        when(studentRepo.save(Mockito.any(Student.class))).thenReturn(student);

        StudentRequest savedStudent = studentService.addStudent(studentDto);

        assert savedStudent != null;

    }



}
