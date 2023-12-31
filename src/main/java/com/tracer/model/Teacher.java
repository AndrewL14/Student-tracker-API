package com.tracer.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Entity
@Table(name = "teachers")
@Data
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Teacher implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Student> students;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Teacher_role_junction", joinColumns = { @JoinColumn(name = "teacher_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Set<Role> authorities;
    private boolean isEmailVerified;

    public Teacher() {
    }

    public Teacher(String username , String email , String password , Set<Student> students , Set<Role> authorities) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.students = students;
        this.authorities = authorities;
        this.isEmailVerified = false;
    }

    public Teacher(String username , String password , Set<Student> students , Set<Role> authorities) {
        this.username = username;
        this.password = password;
        this.students = students;
        this.authorities = authorities;
    }

    public Teacher(String username , String password, String email, Set<Student> students) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.students = students;
    }

    public Teacher(String username , String password , Set<Student> students) {
        this.username = username;
        this.password = password;
        this.students = students;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
