package com.tracer.service;

import com.tracer.model.DTO.PublicStudentDTO;
import com.tracer.model.DTO.TeacherStudentList;
import com.tracer.model.Role;
import com.tracer.model.Student;
import com.tracer.model.Teacher;
import com.tracer.model.assignments.Assignments;
import com.tracer.model.request.student.AddStudentRequest;
import com.tracer.model.request.student.DeleteStudentRequest;
import com.tracer.model.request.student.EditStudentRequest;
import com.tracer.repository.AuthorityRepository;
import com.tracer.repository.StudentRepository;
import com.tracer.repository.TeacherRepository;
import com.tracer.repository.assignments.AssignmentsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.*;

public class TeacherServiceTest {
    private Teacher testTeacher = new Teacher("james", "passowrd");
    private Student testStudent = new Student("Jhon Smith", 2);


    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private AssignmentsRepository assignmentsRepository;
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
        TeacherStudentList response = service.getAllStudentsByTeacherUsername(testTeacher.getUsername());

        // THEN
        assertNotNull(response, "Expected NOT null");
//        assertEquals(1 , response.size(), "Expected response to have size 1 but got: " + response.size());
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
        PublicStudentDTO response = service.getStudentByName(testTeacher.getUsername() , testStudent.getName());

        // THEN
        assertNotNull(response);
    }

    @Test
    public void getStudentByName_InvalidTeacherInvalidStudent_ThrowNullPointer() {
        // GIVEN
        String invalidTeacherUsername = "a null teacher";
        String invalidStudentName = "a null student";

        // WHEN
        // THEN
        assertThrows(NullPointerException.class, () -> {
            service.getStudentByName(invalidTeacherUsername, invalidStudentName);
        });
    }

    @Test
    public void getStudentByName_ValidTeacherNameInvalidStudent_emptyList() {
        // GIVEN
        String invalidStudentName = "a null student";

        // WHEN
        Mockito.when(teacherRepository.findByUsername(testTeacher.getUsername()))
                .thenReturn(Optional.ofNullable(testTeacher));
        PublicStudentDTO response = service.getStudentByName(testTeacher.getUsername() , invalidStudentName);

        // THEN
        assertNotNull(response);
    }

    @Test
    public void addStudent_validRequest_updatedListOfStudents() {
        // GIVEN
        Set<String> subjects = new HashSet<>();
        subjects.add("math");
        testTeacher.setSubjects(subjects);

        List<Student> fakeStudents = List.of(new Student("fake", 1));
        testTeacher.setStudents(fakeStudents);
        AddStudentRequest request = new AddStudentRequest("John Doe", 1, "math", BigDecimal.ONE);
       Mockito.when(teacherRepository.findByUsername(testTeacher.getUsername()))
               .thenReturn(Optional.of(testTeacher));
       Mockito.when(authorityRepository.findByAuthority("STUDENT")).thenReturn(Optional.of(new Role("STUDENT")));
       Mockito.when(assignmentsRepository.save(Mockito.any())).thenReturn(new Assignments());
       Mockito.when(studentRepository.save(Mockito.any())).thenReturn(testStudent);

        // Act
        TeacherStudentList response = service.addStudent(request, "james");

        // Assert
        Mockito.verify(teacherRepository, Mockito.times(1)).save(testTeacher);
    }

    @Test
    public void addStudent_InvalidRequest_throwsNullPointer() {
        // GIVEN
        AddStudentRequest request = new AddStudentRequest("joe", 2, "math", BigDecimal.ONE);

        // WHEN
        // THEN
        assertThrows(NullPointerException.class, () -> {
            service.addStudent(request,"invalidTeacher");
        });
    }

    @Test
    public void editExistingStudent_validRequest_ListOfStudents() {
        // GIVEN
        Long studentId = 1L;
        List<Student> students = new ArrayList<>();
        testStudent.setStudentId(studentId);
        students.add(testStudent);
        testTeacher.setStudents(students);

        EditStudentRequest request = new EditStudentRequest();
        request.setStudentId(studentId);
        request.setNameToChange("kenny");
        request.setPeriodToChange(Optional.of(1));

        // WHEN
        Mockito.when(teacherRepository.findByUsername(Mockito.any()))
                .thenReturn(Optional.ofNullable(testTeacher));
        PublicStudentDTO response = service.editExistingStudent(request);

        // THEN
        assertNotNull(response);
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
            service.editExistingStudent(invalidRequest);
        });
    }

    @Test
    public void editExistingStudent_IncompleteRequest_returnsUpdatedList() {
        // GIVEN
        Long studentId = 1L;
        List<Student> students = new ArrayList<>();
        testStudent.setStudentId(studentId);
        students.add(testStudent);
        testTeacher.setStudents(students);

        EditStudentRequest request = new EditStudentRequest();
        request.setStudentId(studentId);

        // WHEN
        Mockito.when(teacherRepository.findByUsername(testTeacher.getUsername()))
                .thenReturn(Optional.ofNullable(testTeacher));
        PublicStudentDTO response = service.editExistingStudent(request);

        // THEN
        assertNotNull(response);
        Mockito.verify(teacherRepository, Mockito.times(1)).save(testTeacher);
        Mockito.verify(studentRepository, Mockito.times(1)).save(Mockito.any(Student.class));
    }

    @Test
    public void deleteStudent_validRequest() {
        // GIVEN
        Long studentId = 1L;
        List<Student> students = new ArrayList<>();
        testStudent.setStudentId(studentId);
        students.add(testStudent);
        testTeacher.setStudents(students);


        // WHEN
        Mockito.when(teacherRepository.findByUsername(testTeacher.getUsername()))
                .thenReturn(Optional.ofNullable(testTeacher));
        service.deleteStudent(new DeleteStudentRequest(1, studentId), testTeacher.getUsername());

        Mockito.verify(teacherRepository, Mockito.times(1)).save(testTeacher);
        Mockito.verify(studentRepository, Mockito.times(1)).delete(testStudent);
        assertEquals(0, testTeacher.getStudents().size());
    }

    @Test
    public void deleteStudent_InvalidRequest_throwsNullPointer() {
        // GIVEN
        // WHEN
        // THEN
        assertThrows(NullPointerException.class, () -> {
            service.deleteStudent(new DeleteStudentRequest(1, 3L), testTeacher.getUsername());
        });
    }
}
