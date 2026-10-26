package com.company.orderplatform.api;

import com.company.orderplatform.common.exceptions.IlleagelAction;
import com.company.orderplatform.common.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                                     HttpServletRequest request) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new ApiError.FieldError(e.getField(), e.getDefaultMessage()))
                .toList();

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError errorResponse = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                "Request validation failed",
                request.getRequestURI(),
                errors);

        return ResponseEntity.status(status).body(errorResponse);

    }

    @ExceptionHandler(IlleagelAction.class)
    public ResponseEntity<ApiError> handleIlleagelActionException(IlleagelAction ex , HttpServletRequest request){

        HttpStatus status = HttpStatus.CONFLICT;

        ApiError errorResponse = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                List.of()
        );

        //return errorResponse;
        return ResponseEntity.status(status).body(errorResponse);


    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex , HttpServletRequest request){

        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiError errorResponse = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.status(status).body(errorResponse);

    }

}
