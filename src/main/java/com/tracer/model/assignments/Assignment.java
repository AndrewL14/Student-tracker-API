package com.tracer.model.assignments;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "assignment_table")
@Data
@EqualsAndHashCode
@ToString(exclude = "assignmentList")
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private String assignmentType;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "assignments_id")
    private Assignments assignmentList;

    public Assignment(String name , double grade , boolean completed ,
                      boolean overdue , LocalDate dueDate, String assignmentType, Assignments assignmentList) {
        this.name = name;
        this.grade = grade;
        this.completed = completed;
        this.overdue = overdue;
        this.dueDate = dueDate;
        this.assignmentType = assignmentType;
        this.assignmentList = assignmentList;
    }
}
