package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final NotFoundException e) {
        log.error("error = {}, httpStatus = {}", e.getMessage(), HttpStatus.NOT_FOUND);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({MissingRequestHeaderException.class, MethodArgumentNotValidException.class,
            ValidationException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(final Exception e) {
        log.error("error = {}, httpStatus = {}", e.getMessage(), HttpStatus.BAD_REQUEST);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({IllegalStateException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(final Throwable e) {
        log.error("error = {}, httpStatus = {}", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return Map.of("error", e.getMessage());
    }
}