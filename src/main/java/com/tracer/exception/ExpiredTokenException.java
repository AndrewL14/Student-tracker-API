package com.tracer.exception;

import java.io.Serial;

public class ExpiredTokenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8210052284450171757L;

    public ExpiredTokenException(String explanation) {
        super(explanation);
    }

    public ExpiredTokenException() {
    }
}
