package com.tracer.converter;

import com.tracer.model.DTO.PrivateStudentDTO;
import com.tracer.model.DTO.PublicStudentDTO;
import com.tracer.model.DTO.TeacherStudentList;
import com.tracer.model.Student;
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



    private static Map<Integer, Assignments> listToMap(List<Assignments> assignmentsList) {
        Map<Integer, Assignments> response = new HashMap<>();

        for (Assignments assignments : assignmentsList) {
            response.put(assignments.getPeriod(), assignments);
        }

       return response;
    }
}
