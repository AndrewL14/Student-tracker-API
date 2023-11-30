package com.tracer.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "teachers")
@Data
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(unique = true)
    private String username;
    private String password;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Student> students;

    public Teacher() {
    }

    public Teacher(String username , String password , List<Student> students) {
        this.username = username;
        this.password = password;
        this.students = students;
    }
}
