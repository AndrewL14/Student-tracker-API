package com.tracer.converter;

import com.tracer.model.DTO.PrivateStudentDTO;
import com.tracer.model.DTO.PublicStudentDTO;
import com.tracer.model.DTO.TeacherStudentList;
import com.tracer.model.Student;
import com.tracer.model.assignments.Assignment;
import com.tracer.model.assignments.Assignments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ModelConverterTest {
    private Student student1;
    private Student student2;
    private Set<String> subjects;

    @BeforeEach
    void setup() {
        subjects = Set.of("Math", "Science");

        student1 = new Student("John Doe", 2);
        Assignments assignments1 = new Assignments();
        assignments1.setPeriod(2);
        assignments1.setSubject("Math");
        Assignment assignment1 = new Assignment("Homework 1", 90, true, false, LocalDate.now().plusWeeks(1), "Homework", assignments1);
        assignments1.setAssignments(List.of(assignment1));
        student1.setAssignments(List.of(assignments1));

        student2 = new Student("Jane Doe", 1);
        Assignments assignments2 = new Assignments();
        assignments2.setPeriod(1);
        assignments2.setSubject("Science");
        Assignment assignment2 = new Assignment("Homework 1", 90, true, false, LocalDate.now().plusWeeks(1), "Homework", assignments2);
        assignments2.setAssignments(List.of(assignment2));
        student2.setAssignments(List.of(assignments2));
    }


    @Test
    void studentToPrivateDTO_validStudent_PrivateStudentDTO() {
        // GIVEN
        // WHEN
        PrivateStudentDTO result = ModelConverter.studentToPrivateDTO(student1);

        // THEN
        assertNotNull(result);
        assertNotNull(result);
        assertEquals("John Doe", result.name());
        assertEquals(1, result.assignments().size());
        assertEquals("Math", result.assignments().get(2).subject());
    }

    @Test
    void teacherStudentListToDTO_validParams_TeacherStudentList() {
        // GIVEN
        List<Student> students = List.of(student1, student2);

        // WHEN
        TeacherStudentList result = ModelConverter.teacherStudentListToDTO(students, subjects);

        // THEN
        assertNotNull(result);
        assertEquals(2, result.students().size());
        assertEquals(1, result.students().get(2).size());
        assertEquals("John Doe", result.students().get(2).get(0).name());
        assertEquals(1, result.students().get(1).size());
        assertEquals("Jane Doe", result.students().get(1).get(0).name());
    }

    @Test
    void studentToPublicDTO_validParams_PublicStudentDTO() {
        // GIVEN
        // WHEN
        PublicStudentDTO result = ModelConverter.studentToPublicDTO(student1, subjects);

        // THEN
        assertNotNull(result);
        assertEquals("John Doe", result.name());
        assertEquals(2, result.period());
        assertEquals("Math", result.assignments().getSubject());
    }
}
