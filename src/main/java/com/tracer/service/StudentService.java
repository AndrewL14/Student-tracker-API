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

    public List<Assignment> getAllAssignmentsByStudentId(Long id) {
        return studentRepository
                .findById(id)
                .orElseThrow(NullPointerException::new)
                .getAssignments();
    }

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
                request.getAssignmentType()))
        );
        student.setAssignments(assignmentsToUpdate);
        student.setGrade(BigDecimal.valueOf(updateAverageGrade(assignmentsToUpdate)));
        studentRepository.save(student);
        return assignmentsToUpdate;
    }

    public List<Assignment> updateCompleteStatus(Long studentId, Long assignmentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(NullPointerException::new);
        student.getAssignments().stream()
                .filter(a -> Objects.equals(a.getAssignmentId() , assignmentId))
                .findFirst()
                .ifPresent(assignment -> {
                    assignment.setCompleted(true);
                    assignmentRepository.save(assignment);
                });
        studentRepository.save(student);
        return student.getAssignments();
    }

    public List<Assignment> editAssignment(EditAssignmentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(NullPointerException::new);
        List<Assignment> assignmentsToUpdate = student.getAssignments().stream()
                .peek(assignment -> {
                    if (assignment.getAssignmentId().equals(request.getAssignmentId())) {
                        if (!request.getAssignmentNameToChange().isEmpty())
                            assignment.setAssignmentName(request.getAssignmentNameToChange());
                        request.getGradeToChange().ifPresent(grade -> {assignment.setGrade(grade);});
                        request.getDueDateToChange().ifPresent(date -> {assignment.setDueDate(date);});
                        if (!request.getAssignmentNameToChange().isEmpty())
                            assignment.setAssignmentName(request.getAssignmentNameToChange());
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

    public List<Assignment> deleteAssignment(DeleteAssignmentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(NullPointerException::new);
        List<Assignment> assignments = student.getAssignments();
        assignments.removeIf(assignment -> Objects.equals(assignment.getAssignmentId() , request.getAssignmentId()));
        assignmentRepository.deleteById(request.getAssignmentId());
        student.setAssignments(assignments);
        studentRepository.save(student);
        return assignments;
    }

    private double updateAverageGrade(List<Assignment> assignments) {
        double total = 0;
        for (Assignment assignment : assignments) {
            total += assignment.getGrade();
        }
        return  total / assignments.size();
    }
}