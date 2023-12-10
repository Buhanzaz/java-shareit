package shareit.server.exception.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shareit.server.exception.*;

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
        ErrorHandler.ErrorResponse errorResponse = errorHandler.handlerBadRequest(new ValidateException("Test"));

        assertEquals("Test", errorResponse.getError());
    }

    @Test
    void handlerForDataException() {
        ErrorHandler.ErrorResponse errorResponse = errorHandler.handlerBadRequest(new DataTimeException("Test"));

        assertEquals("Test", errorResponse.getError());
    }

    @Test
    void handlerForBookingException() {
        ErrorHandler.ErrorResponse errorResponse = errorHandler.handlerBadRequest(new BookingException("Test"));

        assertEquals("Test", errorResponse.getError());
    }

    @Test
    void handlerForEnumException() {
        ErrorHandler.ErrorResponse errorResponse = errorHandler.handlerBadRequest(new EnumException("Test"));

        assertEquals("Test", errorResponse.getError());
    }
}