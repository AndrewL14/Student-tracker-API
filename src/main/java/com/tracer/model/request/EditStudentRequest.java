package com.tracer.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EditStudentRequest {
    private String teacherUsername;
    private String name;
    private Integer period;
    private Optional<Integer> periodToChange = Optional.empty();
    private Optional<String> nameToChange = Optional.empty();
    private Optional<Double> gradeToChange = Optional.empty();
}
