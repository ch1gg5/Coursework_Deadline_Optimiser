package com.chiggs.coursework_deadline_optimiser.repo;

import com.chiggs.coursework_deadline_optimiser.model.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class StudentRepoTest {

    @Autowired
    private StudentRepo studentRepo;

    @Test
    public void StudentRepo_SaveAll_ReturnSavedStudent(){
        Student student = Student.builder()
                .name("John Doe")
                .email("John@gmail.com")
                .maxHoursPerDay(5).build();

        Student savedStudent = studentRepo.save(student);

        Assertions.assertNotNull(savedStudent);
        Assertions.assertNotNull(savedStudent.getEmail());
        Assertions.assertEquals("John@gmail.com", savedStudent.getEmail());

    }

    @Test
    public void StudentRepo_GetAll_ReturnsMoreThanOneStudent(){
        Student student1 = Student.builder()
                .name("John Doe")
                .email("John@gmail.com")
                .maxHoursPerDay(5).build();

        Student student2 = Student.builder()
                .name("Jane Doe")
                .email("Jane@gmail.com")
                .maxHoursPerDay(3).build();

        studentRepo.save(student1);
        studentRepo.save(student2);

        List<Student> students = studentRepo.findAll();

        Assertions.assertNotNull(students);
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void StudentRepo_FindByEmail_ReturnsStudent(){
        Student student1 = Student.builder()
                .name("John Doe")
                .email("John@gmail.com")
                .maxHoursPerDay(5).build();

        Student student2 = Student.builder()
                .name("Jane Doe")
                .email("Jane@gmail.com")
                .maxHoursPerDay(3).build();

        studentRepo.save(student1);
        studentRepo.save(student2);

        Student test = studentRepo.findByEmail("Jane@gmail.com").orElse(null);
        Assertions.assertNotNull(test);
        Assertions.assertEquals("Jane@gmail.com", test.getEmail());
        Assertions.assertEquals("Jane Doe", test.getName());
    }

}
