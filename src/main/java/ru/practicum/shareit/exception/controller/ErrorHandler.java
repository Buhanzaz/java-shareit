package ru.practicum.shareit.exception.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleForNotFound(final NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error 404 " + exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleForConflict(final ConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Error 409 " + exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleForBadRequest(final ValidateException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error 400 " + exception.getMessage());
    }
}
