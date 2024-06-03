package com.tracer.model.DTO;


import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public record TeacherStudentList(
        Map<Integer, List<PublicStudentDTO>> students
) {
}
