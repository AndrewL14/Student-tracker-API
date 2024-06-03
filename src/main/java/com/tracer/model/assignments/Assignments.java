package com.tracer.model.assignments;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tracer.model.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "assignments_table", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "period"})
})
@Getter
@Setter
@NoArgsConstructor
public class Assignments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "assignments_id")
    private Long id;
    private String subject;
    private int period;
    private double averageGrade;
    @JsonManagedReference
    @OneToMany(mappedBy = "assignmentList", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Assignment> assignments;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Assignments(String subject , int period , Student student) {
        this.subject = subject;
        this.period = period;
        this.averageGrade = 100.0;
        this.assignments = new ArrayList<>();
        this.student = student;
    }
}
