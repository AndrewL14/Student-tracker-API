package com.tracer.constant;

public enum Authority {
    TEACHER("TEACHER"),
    STUDENT("STUDENT"),
    ADMIN("ADMIN");

    private final String label;

    private Authority(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}