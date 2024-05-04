package pl.sonmiike.budgetapp.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.sonmiike.budgetapp.exceptions.custom.ApiError;
import pl.sonmiike.budgetapp.exceptions.custom.EmailAlreadyTakenException;
import pl.sonmiike.budgetapp.exceptions.custom.IdNotMatchingException;
import pl.sonmiike.budgetapp.exceptions.custom.ResourceNotFoundException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class ResponseExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                List.of(e.getMessage()),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({EmailAlreadyTakenException.class, IdNotMatchingException.class})
    public ResponseEntity<ApiError> handleConflict(RuntimeException e, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                List.of(e.getMessage()),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException e, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                List.of(e.getMessage()),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException e, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                List.of(e.getMessage()),
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                errors,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleFormatException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String error = "Invalid date format. Please use the format YYYY-MM-DD.";
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                List.of(error),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
