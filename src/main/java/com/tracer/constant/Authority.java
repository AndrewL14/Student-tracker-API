package com.tracer.constant;

public enum Authority {
    TEACHER("Teacher"),
    STUDENT("Student"),
    ADMIN("Admin");

    private final String label;

    private Authority(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}