package ru.practicum.shareit.exception.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.DataTimeException;
import ru.practicum.shareit.exception.EnumException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ErrorHandlerTest {
    @Autowired
    ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
    }

    @Test
    void handleForNotFound() {
        ErrorHandler.ErrorResponse errorResponse = errorHandler.handleForNotFound(new NotFoundException("Test"));

        assertEquals("Test", errorResponse.getError());
    }

    @Test
    void handleForBadRequest() {
        ErrorHandler.ErrorResponse errorResponse = errorHandler.handleForBadRequest(new ValidateException("Test"));

        assertEquals("Test", errorResponse.getError());
    }

    @Test
    void handlerForDataException() {
        ErrorHandler.ErrorResponse errorResponse = errorHandler.handlerForDataException(new DataTimeException("Test"));

        assertEquals("Test", errorResponse.getError());
    }

    @Test
    void handlerForEnumException() {
        ErrorHandler.ErrorResponse errorResponse = errorHandler.handlerForEnumException(new EnumException("Test"));

        assertEquals("Test", errorResponse.getError());
    }
}