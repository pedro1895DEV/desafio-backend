package com.pedromartins.backend_challenge.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                false, // success
                "Unauthorized", // error
                ex.getMessage(), // message
                "/all"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                false,
                "Internal Server Error",
                ex.getMessage(),
                "/all"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    Exemplo para 403 - Forbidden
//    @ExceptionHandler(ForbiddenException.class)
//    public ResponseEntity<Object> handleForbidden(ForbiddenException ex, HttpServletRequest request) {
//        return buildErrorResponse(HttpStatus.FORBIDDEN, "forbidden", ex.getMessage(), request);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleGeneralException(Exception ex, HttpServletRequest request) {
//        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "internal_error", ex.getMessage(), request);
//    }
//
//    private ResponseEntity<Object> buildErrorResponse(
//            HttpStatus status,
//            String error,
//            String message,
//            HttpServletRequest request
//    ) {
//        ErrorResponse response = new ErrorResponse(false, error, message, request.getRequestURI());
//        return new ResponseEntity<>(response, status);
//    }
}
