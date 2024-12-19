package com.pedromartins.backend_challenge.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private boolean success;
    private String error;
    private String message;
    private String path;
}
