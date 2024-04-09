package com.tracer.service;

import com.tracer.model.Student;
import com.tracer.model.assignments.Assignment;
import com.tracer.model.request.assignment.AddAssignmentRequest;
import com.tracer.model.request.assignment.DeleteAssignmentRequest;
import com.tracer.model.request.assignment.EditAssignmentRequest;
import com.tracer.repository.AssignmentRepository;
import com.tracer.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;

    /**
     * Gets a specific student by their ID.
     * @param id Database Id
     * @return The student associated with the ID.
     */
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(NullPointerException::new);
    }

    /**
     * Gets all assignments associated with a given student.
     * @param id Database id for the student.
     * @return a List of Assignments.
     */
    public List<Assignment> getAllAssignmentsByStudentId(Long id) {
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
    public List<Assignment> addNewAssignment(AddAssignmentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(NullPointerException::new);
        List<Assignment> assignmentsToUpdate = student.getAssignments();

        assignmentsToUpdate.add(assignmentRepository.save(new Assignment(
                request.getAssignmentName(),
                request.getGrade(),
                request.isCompleted(),
                false,
                request.getDueDate(),
                request.getAssignmentType().toUpperCase()))
        );
        student.setAssignments(assignmentsToUpdate);
        student.setGrade(BigDecimal.valueOf(updateAverageGrade(assignmentsToUpdate)));
        studentRepository.save(student);
        return assignmentsToUpdate;
    }

    /**
     * preforms a status update on the assignment to mark the assignment as complete.
     * @param studentId A students Database ID.
     * @param assignmentId The specific assignment ID.
     * @return the updated List of assignments.
     */
    public List<Assignment> updateCompleteStatus(Long studentId, Long assignmentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(NullPointerException::new);
        student.getAssignments().stream()
                .filter(a -> Objects.equals(a.getId() , assignmentId))
                .findFirst()
                .ifPresent(assignment -> {
                    assignment.setCompleted(true);
                    assignmentRepository.save(assignment);
                });
        studentRepository.save(student);
        return student.getAssignments();
    }

    /**
     * Updates an assignment with information given by the user.
     * @param request information needed to locate and update the assignment.
     * @return Update list of assignments.
     */
    public List<Assignment> editAssignment(EditAssignmentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(NullPointerException::new);
        List<Assignment> assignmentsToUpdate = student.getAssignments().stream()
                .peek(assignment -> {
                    if (assignment.getId().equals(request.getAssignmentId())) {
                        if (!request.getAssignmentNameToChange().isEmpty())
                            assignment.setName(request.getAssignmentNameToChange());
                        request.getGradeToChange().ifPresent(grade -> {assignment.setGrade(grade);});
                        request.getDueDateToChange().ifPresent(date -> {assignment.setDueDate(date);});
                        if (!request.getAssignmentTypeToChange().isEmpty()) {
                            assignment.setAssignmentType(request.getAssignmentTypeToChange());
                        }
                        assignmentRepository.save(assignment);
                    }
                }).toList();
        student.setGrade(BigDecimal.valueOf(updateAverageGrade(assignmentsToUpdate)));
        studentRepository.save(student);
        return assignmentsToUpdate;
    }

    /**
     * Deletes an assignment.
     * @param request Information to locate the student and assignment.
     * @return Update List of assignments.
     */
    public List<Assignment> deleteAssignment(DeleteAssignmentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(NullPointerException::new);
        List<Assignment> assignments = student.getAssignments();
        assignments.removeIf(assignment -> Objects.equals(assignment.getId() , request.getAssignmentId()));
        assignmentRepository.deleteById(request.getAssignmentId());
        student.setAssignments(assignments);
        student.setGrade(BigDecimal.valueOf(updateAverageGrade(assignments)));
        studentRepository.save(student);
        return assignments;
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