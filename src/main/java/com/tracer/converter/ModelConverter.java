package com.tracer.converter;

import com.tracer.model.DTO.*;
import com.tracer.model.Student;
import com.tracer.model.assignments.Assignment;
import com.tracer.model.assignments.Assignments;

import java.util.*;
import java.util.stream.Collectors;

public class ModelConverter {

    /**
     * Converts a student object into a private student containing the students name and a custom map organized by subject/period
     * @param student A unaltered student model.
     * @return PrivateStudentDTO containing a custom map of Assignments and the student name.
     */
    public static PrivateStudentDTO studentToPrivateDTO(Student student) {
        return new PrivateStudentDTO(student.getName(),
                listToMap(student.getAssignments()));
    }

    /**
     * takes the teachers list of students and organizes them by period, while also making sure
     * the assignments are only for that teachers class.
     * @param students An unordered list of students.
     * @param subjects A set of subjects the teacher teaches.
     * @return A TeacherStudentList a Map of students organized by period.
     */
    public static TeacherStudentList teacherStudentListToDTO(List<Student> students, Set<String> subjects) {
        Map<Integer, List<PublicStudentDTO>> response = students.stream()
                .map(student -> studentToPublicDTO(student, subjects))
                .collect(Collectors.groupingBy(PublicStudentDTO::period));

        return new TeacherStudentList(response);
    }

    /**
     * Takes all assignments in a student then pulls only the assignments that have the same subject
     * in subjects.
     * @param student A student model.
     * @param subjects A set of strings representing the subjects a teacher teaches.
     * @return A PublicStudentDTO Containing proper Assignments list.
     */
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


    /**
     * Converts an unordered list of assignments into a map of StudentAssignmentsDTO
     * organized by period.
     * @param assignmentsList an unordered list of assignments
     * @return a Map of studentAssignmentsDTO organized by period.
     */
    private static Map<Integer, StudentAssignmentsDTO> listToMap(List<Assignments> assignmentsList) {
        Map<Integer, StudentAssignmentsDTO> response = new HashMap<>();

        for (Assignments assignments : assignmentsList) {
            response.put(assignments.getPeriod(), assignmentsToDTO(assignments));
        }

       return response;
    }

    /**
     * Converts an Assignments object into a dto.
     * @param assignments object.
     * @return A StudentAssignmentsDTO.
     */
    private static StudentAssignmentsDTO assignmentsToDTO(Assignments assignments) {
        return new StudentAssignmentsDTO(
                assignments.getSubject(),
                assignments.getPeriod(),
                assignments.getAverageGrade(),
                assignmentToDTO(assignments.getAssignments())
        );
    }

    /**
     * Converts a List of Assignment objects into a list of DTOs.
     * @param assignments A list of Assignment objects.
     * @return A list of Assignment DTOs.
     */
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
