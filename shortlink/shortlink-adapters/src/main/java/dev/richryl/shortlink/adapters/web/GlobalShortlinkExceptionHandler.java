package dev.richryl.shortlink.adapters.web;

import dev.richryl.shortlink.adapters.web.dto.ErrorResponse;
import dev.richryl.shortlink.adapters.web.dto.ValidationErrorResponse;
import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(ShortlinkNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleShortlinkNotFoundException(ShortlinkNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                404,
                "SHORTLINK_NOT_FOUND",
                e.getMessage()
        );
        return ResponseEntity.status(404).body(errorResponse);
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                400,
                "INVALID_PATH_VARIABLE",
                                "The path variable '" + e.getName() + "' has an invalid value: " + e.getValue() + ". Expected type: " + (e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : null)
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
