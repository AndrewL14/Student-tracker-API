package com.tracer.model.assignments;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "assignments")
@Data
@EqualsAndHashCode
@ToString
@Getter
@Setter
@NoArgsConstructor
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "assignment_id")
    private Long id;
    private String name;
    private Double grade;
    private boolean completed;
    private boolean overdue;
    private LocalDate dueDate;
    private String assignmentType;

    public Assignment(String name , double grade , boolean completed ,
                      boolean overdue , LocalDate dueDate, String assignmentType) {
        this.name = name;
        this.grade = grade;
        this.completed = completed;
        this.overdue = overdue;
        this.dueDate = dueDate;
        this.assignmentType = assignmentType;
    }
}
