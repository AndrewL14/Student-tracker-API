package com.tracer.service;

import com.tracer.model.users.Student;
import com.tracer.model.assignments.Assignment;
import com.tracer.model.assignments.Assignments;
import com.tracer.model.request.assignment.AddAssignmentRequest;
import com.tracer.model.request.assignment.DeleteAssignmentRequest;
import com.tracer.model.request.assignment.EditAssignmentRequest;
import com.tracer.model.request.assignment.UpdateAssignmentStatusRequest;
import com.tracer.repository.StudentRepository;
import com.tracer.repository.assignments.AssignmentRepository;
import com.tracer.repository.assignments.AssignmentsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class AssignmentsService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private AssignmentsRepository assignmentsRepository;

    /**
     * Gets all assignments associated with a given student.
     * @param id Database id for the student.
     * @return a List of Assignments.
     */
    public List<Assignments> getAllAssignmentsByStudentId(Long id) {
        return studentRepository
                .findById(id)
                .orElseThrow(NullPointerException::new)
                .getAssignments();
    }

    /**
     * Creates a new Assignment updates the student's assignment list and returns the
     * updated list.
     * @param request Information for a new assignment object.
     * @return Updated list of assignments.
     */
    public Assignments addNewAssignment(AddAssignmentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new NoSuchElementException("Student not found"));

        List<Assignments> originalList = student.getAssignments();
        Assignments assignments = null;

        for (Assignments assignmentList : originalList) {
            if (assignmentList.getPeriod() == request.getPeriod()) {
                assignments = assignmentList;
                break;
            }
        }

        Assignment newAssignment = new Assignment(
                request.getAssignmentName(),
                request.getGrade(),
                request.isCompleted(),
                false,
                request.getDueDate(),
                request.getAssignmentType().toUpperCase(),
                assignments
        );
        assignmentRepository.save(newAssignment);

        List<Assignment> assignmentsToUpdate = new ArrayList<>(assignments.getAssignments());
        assignmentsToUpdate.add(newAssignment);
        assignments.setAssignments(assignmentsToUpdate);
        assignments.setAverageGrade(updateAverageGrade(assignmentsToUpdate));

        student.setAssignments(originalList);
        studentRepository.save(student);

        return assignments;
    }

    /**
     * preforms a status update on the assignment to mark the assignment as complete.
     * @param request user request containing the period, student, and assignmentId.
     * @return the updated List of assignments.
     */
    public List<Assignment> updateCompleteStatus(UpdateAssignmentStatusRequest request, String teacherUsername) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalStateException("Student not found"));

        List<Assignments> originalList = student.getAssignments();

        if (request.getPeriod() < 0) {
            throw new IllegalStateException("No assignments found for the specified period");
        }

        Assignments assignmentList = originalList.stream().filter(assignments -> assignments.getPeriod() == request.getPeriod())
                .findAny().orElseThrow(NullPointerException::new);

        Assignment assignmentToUpdate = assignmentList.getAssignments()
                .stream()
                .filter(a -> Objects.equals(a.getId(), request.getAssignmentId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Assignment not found"));

        assignmentToUpdate.setCompleted(true);
        assignmentRepository.save(assignmentToUpdate);

        studentRepository.save(student);

        return assignmentList.getAssignments();
    }

    /**
     * Updates an assignment with information given by the user.
     * @param request information needed to locate and update the assignment.
     * @return Update list of assignments.
     */
    public List<Assignment> editAssignment(EditAssignmentRequest request) {
        Assignment assignment = assignmentRepository.findById(request.getAssignmentId())
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with ID: " + request.getAssignmentId()));
        if (!request.getAssignmentNameToChange().isEmpty()) {
            assignment.setName(request.getAssignmentNameToChange());
        }
        request.getGradeToChange().ifPresent(assignment::setGrade);
        request.getDueDateToChange().ifPresent(assignment::setDueDate);
        if (!request.getAssignmentTypeToChange().isEmpty()) {
            assignment.setAssignmentType(request.getAssignmentTypeToChange());
        }
        assignmentRepository.save(assignment);
        Assignments assignments = assignment.getAssignmentList();
        assignments.setAverageGrade(updateAverageGrade(assignments.getAssignments()));
        assignmentsRepository.save(assignments);
        return assignments.getAssignments();
    }

    /**
     * Deletes an assignment.
     * @param request Information to locate the student and assignment.
     * @return Update List of assignments.
     */
    public List<Assignment> deleteAssignment(DeleteAssignmentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + request.getStudentId()));
        List<Assignments> originalList = student.getAssignments();
        Assignments assignments = originalList.stream().filter(as -> as.getPeriod() == request.getPeriod()).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("no Assignments for period: %d", request.getPeriod())));
        List<Assignment> assignmentsList = new ArrayList<>(assignments.getAssignments());
        boolean removed = assignmentsList.removeIf(assignment ->
                Objects.equals(assignment.getId(), request.getAssignmentId()));
        if (removed) {
            assignmentRepository.deleteById(request.getAssignmentId());
        } else {
            throw new EntityNotFoundException("Assignment not found with ID: " + request.getAssignmentId());
        }
        assignments.setAverageGrade(updateAverageGrade(assignments.getAssignments()));
        student.setAssignments(originalList);
        studentRepository.save(student);
        assignments.setAssignments(assignmentsList);
        return assignments.getAssignments();
    }



    /**
     * Updates the average grade of all assignments.
     * @param assignments List of assignments to get all grades.
     * @return the average grade out of all grades given.
     */
    private double updateAverageGrade(List<Assignment> assignments) {
        double total = 0;
        for (Assignment assignment : assignments) {
            total += assignment.getGrade();
        }
        return  total / assignments.size();
    }
}
