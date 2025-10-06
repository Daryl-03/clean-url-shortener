package dev.richryl.shortlink.adapaters.web;

import dev.richryl.shortlink.adapaters.web.dto.ErrorResponse;
import dev.richryl.shortlink.adapaters.web.dto.ValidationErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalShortlinkExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                400,
                "VALIDATION_ERROR",
                "Validation failed for the request",
                e.getFieldErrors().stream()
                        .map(fieldError -> new ValidationErrorResponse.ValidationField(fieldError.getField(), fieldError.getDefaultMessage()))
                        .toList()
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                400,
                "MISSING_REQUEST_BODY",
                "The request body is malformed or unreadable"
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
