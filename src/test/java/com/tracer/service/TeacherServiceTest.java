package com.tracer.service;

import com.tracer.model.Role;
import com.tracer.model.Teacher;
import com.tracer.model.request.teacher.CreateTeacherRequest;
import com.tracer.model.request.teacher.EditTeacherRequest;
import com.tracer.repository.AuthorityRepository;
import com.tracer.repository.StudentRepository;
import com.tracer.repository.TeacherRepository;
import com.tracer.repository.assignments.AssignmentsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private AssignmentsRepository assignmentsRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher testTeacher;
    private CreateTeacherRequest createTeacherRequest;
    private EditTeacherRequest editTeacherRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testTeacher = new Teacher();
        testTeacher.setTeacherId(1L);
        testTeacher.setUsername("teacher1");
        testTeacher.setEmail("teacher1@example.com");
        testTeacher.setPassword("password");
        testTeacher.setSubjects(new HashSet<>(Set.of("Math", "Science")));

        createTeacherRequest = new CreateTeacherRequest("teacher1", "teacher1@example.com", "password", Set.of("Math", "Science"));

        editTeacherRequest = new EditTeacherRequest();
        editTeacherRequest.setUsername("newTeacher");
        editTeacherRequest.setEmail("newTeacher@example.com");
        editTeacherRequest.setPassword("newPassword");
        editTeacherRequest.setSubjects(new HashSet<>(Set.of("Math")));
    }

    @Test
    public void createTeacher_validRequest_teacher() {
        // GIVEN
        Role teacherRole = new Role();
        teacherRole.setAuthority("TEACHER");

        // WHEN
        when(authorityRepository.findByAuthority("TEACHER")).thenReturn(Optional.of(teacherRole));

        Teacher createdTeacher = teacherService.createTeacher(createTeacherRequest);

        // THEN
        assertNotNull(createdTeacher);
        assertEquals(createTeacherRequest.username(), createdTeacher.getUsername());
        assertEquals(createTeacherRequest.email(), createdTeacher.getEmail());
        assertEquals(createTeacherRequest.password(), createdTeacher.getPassword());
        assertEquals(createTeacherRequest.subjects(), createdTeacher.getSubjects());

        verify(authorityRepository, times(1)).findByAuthority("TEACHER");
        verify(teacherRepository, times(1)).save(createdTeacher);
    }

    @Test
    public void editTeacher_ValidRequest_teacher() {
        // GIVEN
        // WHEN
        when(teacherRepository.findByUsername("teacher1")).thenReturn(Optional.of(testTeacher));

        Teacher editedTeacher = teacherService.editTeacher(editTeacherRequest, "teacher1");

        // THEN
        assertNotNull(editedTeacher);
        assertEquals(editTeacherRequest.getUsername(), editedTeacher.getUsername());
        assertEquals(editTeacherRequest.getEmail(), editedTeacher.getEmail());
        assertEquals(editTeacherRequest.getPassword(), editedTeacher.getPassword());
        assertEquals(editTeacherRequest.getSubjects(), editedTeacher.getSubjects());

        verify(teacherRepository, times(1)).findByUsername("teacher1");
        verify(teacherRepository, times(1)).save(editedTeacher);
    }

    @Test
    public void deleteTeacherByUsername_ValidUsername_void() {
        // GIVEN
        // WHEN
        when(teacherRepository.findByUsername("teacher1")).thenReturn(Optional.of(testTeacher));

        teacherService.deleteTeacherByUsername("teacher1");

        // THEN
        verify(teacherRepository, times(1)).findByUsername("teacher1");
        verify(teacherRepository, times(1)).delete(testTeacher);
    }

    @Test
    public void deleteTeacherById_validId_void() {
        // GIVEN
        // WHEN
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));

        teacherService.deleteTeacherById(1L);

        // THEN
        verify(teacherRepository, times(1)).findById(1L);
        verify(teacherRepository, times(1)).delete(testTeacher);
    }

    @Test
    public void loadUserByUsername_validUsername_userDetails() {
        // GIVEN
        // WHEN
        when(teacherRepository.findByUsername("teacher1")).thenReturn(Optional.of(testTeacher));

        UserDetails userDetails = teacherService.loadUserByUsername("teacher1");

        // THEN
        assertNotNull(userDetails);
        assertEquals(testTeacher, userDetails);

        verify(teacherRepository, times(1)).findByUsername("teacher1");
    }

    @Test
    public void loadUserByEmail_validEmail_userDetails() {
        // GIVEN
        // WHEN
        when(teacherRepository.findByEmail("teacher1@example.com")).thenReturn(Optional.of(testTeacher));

        UserDetails userDetails = teacherService.loadUserByEmail("teacher1@example.com");

        // THEN
        assertNotNull(userDetails);
        assertEquals(testTeacher, userDetails);

        verify(teacherRepository, times(1)).findByEmail("teacher1@example.com");
    }

    @Test
    public void saveNewTeacher_validTeacher_userDetails() {
        // GIVEN
        // WHEN
        when(teacherRepository.save(testTeacher)).thenReturn(testTeacher);

        UserDetails userDetails = teacherService.saveNewTeacher(testTeacher);

        // THEN
        assertNotNull(userDetails);
        assertEquals(testTeacher, userDetails);

        verify(teacherRepository, times(1)).save(testTeacher);
    }
}