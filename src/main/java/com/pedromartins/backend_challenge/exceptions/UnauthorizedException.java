package com.pedromartins.backend_challenge.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("User unauthorized");
    }

    public UnauthorizedException(String message) { super(message);}
}
