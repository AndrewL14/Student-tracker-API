package com.tracer.service;

import com.tracer.model.Student;
import com.tracer.model.assignments.Assignment;
import com.tracer.model.assignments.Assignments;
import com.tracer.model.request.assignment.AddAssignmentRequest;
import com.tracer.model.request.assignment.DeleteAssignmentRequest;
import com.tracer.model.request.assignment.EditAssignmentRequest;
import com.tracer.model.request.assignment.UpdateAssignmentStatusRequest;
import com.tracer.repository.StudentRepository;
import com.tracer.repository.assignments.AssignmentRepository;
import com.tracer.repository.assignments.AssignmentsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class AssignmentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private AssignmentsRepository assignmentsRepository;
    @Mock
    private AssignmentRepository assignmentRepository;

    @InjectMocks
    private AssignmentsService service;

    private Student testStudent;
    private Assignments testAssignments;
    private Assignment testAssignment;
    private AddAssignmentRequest addAssignmentRequest;
    private UpdateAssignmentStatusRequest updateAssignmentStatusRequest;
    private EditAssignmentRequest editAssignmentRequest;
    private DeleteAssignmentRequest deleteAssignmentRequest;

    @BeforeEach
    public void setup() {
        openMocks(this);

        testAssignment = new Assignment();
        testAssignment.setId(1L);
        testAssignment.setGrade(90.0);
        testAssignment.setCompleted(false);

        testAssignments = new Assignments();
        testAssignments.setId(1L);
        testAssignments.setPeriod(1);
        testAssignments.setAssignments(List.of(testAssignment));
        testAssignment.setAssignmentList(testAssignments);

        testStudent = new Student();
        testStudent.setStudentId(1L);
        testStudent.setName("test student");
        testStudent.setAssignments(List.of(testAssignments));
        testAssignments.setStudent(testStudent);

        addAssignmentRequest = new AddAssignmentRequest();
        addAssignmentRequest.setStudentId(1L);
        addAssignmentRequest.setPeriod(1);
        addAssignmentRequest.setAssignmentName("New Assignment");
        addAssignmentRequest.setGrade(95.0);
        addAssignmentRequest.setCompleted(false);
        addAssignmentRequest.setDueDate(LocalDate.now());
        addAssignmentRequest.setAssignmentType("HOMEWORK");

        updateAssignmentStatusRequest = new UpdateAssignmentStatusRequest();
        updateAssignmentStatusRequest.setStudentId(1L);
        updateAssignmentStatusRequest.setPeriod(1);
        updateAssignmentStatusRequest.setAssignmentId(1L);

        editAssignmentRequest = new EditAssignmentRequest();
        editAssignmentRequest.setAssignmentId(1L);
        editAssignmentRequest.setAssignmentNameToChange("Updated Assignment");

        deleteAssignmentRequest = new DeleteAssignmentRequest();
        deleteAssignmentRequest.setStudentId(1L);
        deleteAssignmentRequest.setPeriod(1);
        deleteAssignmentRequest.setAssignmentId(1L);
    }

    @Test
    public void getAllAssignmentsByStudentId_validRequest_returnsAssignments() {
        // GIVEN
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // WHEN
        List<Assignments> assignments = service.getAllAssignmentsByStudentId(1L);

        // THEN
        assertNotNull(assignments, "Expected assignments to not be null");
        assertEquals(1, assignments.size(), "Expected one assignment period");
        assertEquals(1, assignments.get(0).getPeriod(), "Expected period to be 1");
    }

    @Test
    public void addNewAssignment_validRequest_updatesAndReturnsAssignments() {
        // GIVEN
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(assignmentRepository.save(Mockito.any())).thenReturn(testAssignment);

        // WHEN
        Assignments updatedAssignments = service.addNewAssignment(addAssignmentRequest);

        // THEN
        assertNotNull(updatedAssignments, "Expected updated assignments to not be null");
        assertEquals(2, updatedAssignments.getAssignments().size(), "Expected two assignments in the period");
    }

    @Test
    public void updateCompleteStatus_validRequest_marksAssignmentAsComplete() {
        // GIVEN
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // WHEN
        List<Assignment> updatedAssignments = service.updateCompleteStatus(updateAssignmentStatusRequest, "teacherUsername");

        // THEN
        assertNotNull(updatedAssignments, "Expected updated assignments to not be null");
        assertTrue(updatedAssignments.get(0).isCompleted(), "Expected assignment to be marked as complete");
    }

    @Test
    public void editAssignment_validRequest_updatesAssignment() {
        // GIVEN
        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(testAssignment));
        when(assignmentRepository.save(Mockito.any())).thenReturn(testAssignment);

        // WHEN
        List<Assignment> updatedAssignments = service.editAssignment(editAssignmentRequest);

        // THEN
        assertNotNull(updatedAssignments, "Expected updated assignments to not be null");
        assertEquals("Updated Assignment", updatedAssignments.get(0).getName(), "Expected assignment name to be updated");
    }

    @Test
    public void deleteAssignment_validRequest_removesAssignment() {
        // GIVEN
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // WHEN
        List<Assignment> updatedAssignments = service.deleteAssignment(deleteAssignmentRequest);

        // THEN
        assertNotNull(updatedAssignments, "Expected updated assignments to not be null");
        assertTrue(updatedAssignments.isEmpty(), "Expected assignment list to be empty after deletion");
    }
}
