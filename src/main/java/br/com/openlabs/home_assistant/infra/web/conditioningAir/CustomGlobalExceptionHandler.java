package br.com.openlabs.home_assistant.infra.web.conditioningAir;

import br.com.openlabs.home_assistant.infra.web.CustomResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new CustomResponseEntity<>(HttpStatus.BAD_REQUEST, "Validation failed", HttpStatus.BAD_REQUEST.value(), errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomResponseEntity<String> handleAllExceptions(Exception ex) {
        return new CustomResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }


}