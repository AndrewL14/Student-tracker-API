package com.tracer.service;

import com.tracer.model.Student;
import com.tracer.model.Teacher;
import com.tracer.model.request.AddStudentRequest;
import com.tracer.model.request.EditStudentRequest;
import com.tracer.model.request.GetStudentRequest;
import com.tracer.repository.StudentRepository;
import com.tracer.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.*;

public class TeacherServiceTest {
    private Teacher testTeacher = new Teacher("james", "passowrd", new ArrayList<>());
    private Student testStudent = new Student("jhon", 2, BigDecimal.ONE);


    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private TeacherService service;

    @BeforeEach
    public void setup() {
        initMocks(this);
    }

    @Test
    public void getAllStudentsByTeacherUsername_ExistingTeacher_listOfStudents() {
        // GIVEN
        List<Student> students = new ArrayList<>();
        students.add(testStudent);
        testTeacher.setStudents(students);


        // WHEN
        Mockito.when(teacherRepository.findByUsername(testTeacher.getUsername()))
                .thenReturn(Optional.ofNullable(testTeacher));
        List<Student> response = service.getAllStudentsByTeacherUsername(testTeacher.getUsername());

        // THEN
        assertNotNull(response, "Expected NOT null");
        assertEquals(1 , response.size(), "Expected response to have size 1 but got: " + response.size());
    }

    @Test
    public void getAllStudentsByTeacherUsername_NonExistingTeacher_throwNullPointer() {
        // GIVEN
        String invalidTeacherUsername = "a null teacher";

        // WHEN
        // THEN
        assertThrows(NullPointerException.class, () -> {
            service.getAllStudentsByTeacherUsername(invalidTeacherUsername);
        });
    }

    @Test
    public void getStudentByName_validTeacherValidStudent_ListOfStudents() {
        // GIVEN
        List<Student> students = new ArrayList<>();
        students.add(testStudent);
        testTeacher.setStudents(students);

        // WHEN
        Mockito.when(teacherRepository.findByUsername(testTeacher.getUsername()))
                .thenReturn(Optional.ofNullable(testTeacher));
        List<Student> response = service.getStudentsByName(testTeacher.getUsername() , testStudent.getName());

        // THEN
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(response.get(0), testStudent);
    }

    @Test
    public void getStudentByName_InvalidTeacherInvalidStudent_ThrowNullPointer() {
        // GIVEN
        String invalidTeacherUsername = "a null teacher";
        String invalidStudentName = "a null student";

        // WHEN
        // THEN
        assertThrows(NullPointerException.class, () -> {
            service.getStudentsByName(invalidTeacherUsername, invalidStudentName);
        });
    }

    @Test
    public void getStudentByName_ValidTeacherNameInvalidStudent_emptyList() {
        // GIVEN
        String invalidStudentName = "a null student";

        // WHEN
        Mockito.when(teacherRepository.findByUsername(testTeacher.getUsername()))
                .thenReturn(Optional.ofNullable(testTeacher));
        List<Student> response = service.getStudentsByName(testTeacher.getUsername() , invalidStudentName);

        // THEN
        assertNotNull(response);
        assertEquals(0, response.size());
    }

    @Test
    public void addStudent_validRequest_updatedListOfStudents() {
        // GIVEN
        AddStudentRequest request = new AddStudentRequest("John Doe", 1,BigDecimal.ONE);
       Mockito.when(teacherRepository.findByUsername(testTeacher.getUsername()))
               .thenReturn(Optional.of(testTeacher));

        // Act
        List<Student> response = service.addStudent(request, "james");

        // Assert
        assertEquals(1, response.size());
        Mockito.verify(teacherRepository, Mockito.times(1)).save(testTeacher);
        Mockito.verify(studentRepository, Mockito.times(1)).save(Mockito.any(Student.class));
    }

    @Test
    public void addStudent_InvalidRequest_throwsNullPointer() {
        // GIVEN
        AddStudentRequest request = new AddStudentRequest("joe", 2, BigDecimal.ONE);

        // WHEN
        // THEN
        assertThrows(NullPointerException.class, () -> {
            service.addStudent(request,"invalidTeacher");
        });
    }

    @Test
    public void editExistingStudent_validRequest_ListOfStudents() {
        // GIVEN
        List<Student> students = new ArrayList<>();
        students.add(testStudent);
        testTeacher.setStudents(students);

        EditStudentRequest request = new EditStudentRequest();
        request.setName("jhon");
        request.setPeriod(2);
        request.setGradeToChange(Optional.of(10.0));
        request.setNameToChange(Optional.of("kenny"));
        request.setPeriodToChange(Optional.of(1));

        // WHEN
        Mockito.when(teacherRepository.findByUsername(Mockito.any()))
                .thenReturn(Optional.ofNullable(testTeacher));
        List<Student> response = service.editExistingStudent(request, "james");

        // THEN
        assertNotNull(response);
        Student updatedStudent = response.get(0);
        assertEquals(1, response.size());
        assertEquals("kenny", updatedStudent.getName());
        assertEquals(BigDecimal.valueOf(10.0), updatedStudent.getGrade());
        assertEquals(1, updatedStudent.getPeriod());
        Mockito.verify(teacherRepository, Mockito.times(1)).save(testTeacher);
        Mockito.verify(studentRepository, Mockito.times(1)).save(Mockito.any(Student.class));
    }

    @Test
    public void editExistingStudent_InvalidRequest_throwsIllegalArgument() {
        // GIVEN
        EditStudentRequest invalidRequest = new EditStudentRequest();

        // WHEN
        // THEN
        assertThrows(IllegalArgumentException.class, () -> {
            service.editExistingStudent(invalidRequest, "InvalidTeacher");
        });
    }

    @Test
    public void editExistingStudent_IncompleteRequest_returnsUpdatedList() {
        // GIVEN
        List<Student> students = new ArrayList<>();
        students.add(testStudent);
        testTeacher.setStudents(students);

        EditStudentRequest request = new EditStudentRequest();
        request.setName("jhon");
        request.setPeriod(2);
        request.setGradeToChange(Optional.of(10.0));

        // WHEN
        Mockito.when(teacherRepository.findByUsername(testTeacher.getUsername()))
                .thenReturn(Optional.ofNullable(testTeacher));
        List<Student> response = service.editExistingStudent(request, testTeacher.getUsername());

        // THEN
        assertNotNull(response);
        Student updatedStudent = response.get(0);
        assertEquals(1, response.size());
        assertEquals("jhon", updatedStudent.getName(), "expected name to remain the same");
        assertEquals(BigDecimal.valueOf(10.0), updatedStudent.getGrade());
        assertEquals(2, updatedStudent.getPeriod(), "expected period to remain the same");
        Mockito.verify(teacherRepository, Mockito.times(1)).save(testTeacher);
        Mockito.verify(studentRepository, Mockito.times(1)).save(Mockito.any(Student.class));
    }

    @Test
    public void deleteStudent_validRequest() {
        // GIVEN
        List<Student> students = new ArrayList<>();
        students.add(testStudent);
        testTeacher.setStudents(students);
        GetStudentRequest request = new GetStudentRequest(testTeacher.getUsername() , testStudent.getName());

        // WHEN
        Mockito.when(teacherRepository.findByUsername(testTeacher.getUsername()))
                .thenReturn(Optional.ofNullable(testTeacher));
        service.deleteStudent(request, testTeacher.getUsername());

        Mockito.verify(teacherRepository, Mockito.times(1)).save(testTeacher);
        Mockito.verify(studentRepository, Mockito.times(1)).delete(testStudent);
        assertEquals(0, testTeacher.getStudents().size());
    }

    @Test
    public void deleteStudent_InvalidRequest_throwsNullPointer() {
        // GIVEN
        GetStudentRequest invalidRequest = new GetStudentRequest("invalidTeacher", "InvalidStudent");

        // WHEN
        // THEN
        assertThrows(NullPointerException.class, () -> {
            service.deleteStudent(invalidRequest, testTeacher.getUsername());
        });
    }

    @Test
    public void deleteStudent_IncompleteRequest() {
        // GIVEN
        List<Student> students = new ArrayList<>();
        students.add(testStudent);
        testTeacher.setStudents(students);
        GetStudentRequest request = new GetStudentRequest(testTeacher.getUsername() , "InvalidStudent");

        // THEN
        Mockito.when(teacherRepository.findByUsername(testTeacher.getUsername()))
                .thenReturn(Optional.ofNullable(testTeacher));
        service.deleteStudent(request, testTeacher.getUsername());

        // THEN
        assertEquals(1, testTeacher.getStudents().size());
        Mockito.verify(teacherRepository, Mockito.times(0)).save(testTeacher);
        Mockito.verify(studentRepository, Mockito.times(0)).delete(testStudent);
    }

}
