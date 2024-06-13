package com.tracer.converter;

import com.tracer.model.DTO.*;
import com.tracer.model.Student;
import com.tracer.model.assignments.Assignment;
import com.tracer.model.assignments.Assignments;

import java.util.*;
import java.util.stream.Collectors;

public class ModelConverter {

    public static PrivateStudentDTO studentToPrivateDTO(Student student) {
        return new PrivateStudentDTO(student.getName(),
                listToMap(student.getAssignments()));
    }

    public static TeacherStudentList teacherStudentListToDTO(List<Student> students, Set<String> subjects) {
        Map<Integer, List<PublicStudentDTO>> response = students.stream()
                .map(student -> studentToPublicDTO(student, subjects))
                .collect(Collectors.groupingBy(PublicStudentDTO::period));

        return new TeacherStudentList(response);
    }

    public static PublicStudentDTO studentToPublicDTO(Student student, Set<String> subjects) {
        if (subjects == null || subjects.isEmpty()) throw new IllegalArgumentException("subjects is empty");
        Assignments selectedAssignment = student.getAssignments().stream()
                .filter(assignment -> subjects.contains(assignment.getSubject()))
                .findFirst()
                .orElse(new Assignments());

        return new PublicStudentDTO(
                student.getStudentId(),
                student.getName(),
                selectedAssignment.getPeriod(),
                selectedAssignment
        );
    }



    private static Map<Integer, StudentAssignmentsDTO> listToMap(List<Assignments> assignmentsList) {
        Map<Integer, StudentAssignmentsDTO> response = new HashMap<>();

        for (Assignments assignments : assignmentsList) {
            response.put(assignments.getPeriod(), assignmentsToDTO(assignments));
        }

       return response;
    }

    private static StudentAssignmentsDTO assignmentsToDTO(Assignments assignments) {
        return new StudentAssignmentsDTO(
                assignments.getSubject(),
                assignments.getPeriod(),
                assignments.getAverageGrade(),
                assignmentToDTO(assignments.getAssignments())
        );
    }

    private static List<StudentAssignmentDTO> assignmentToDTO(List<Assignment> assignments) {
        List<StudentAssignmentDTO> response = new ArrayList<>();

        for (Assignment assignment : assignments) {
            response.add(
                    new StudentAssignmentDTO(
                            assignment.getName(),
                            assignment.getGrade(),
                            assignment.isCompleted(),
                            assignment.isOverdue(),
                            assignment.getAssignmentType()
                    )
            );
        }
        return response;
    }

}
