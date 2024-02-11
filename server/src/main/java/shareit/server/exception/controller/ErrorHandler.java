package shareit.server.exception.controller;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shareit.server.exception.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleForNotFound(final NotFoundException exception) {
        log.debug("Получен статус 404 Not found {}", exception.getMessage(), exception);
        return ErrorResponse.builder().error(exception.getMessage()).build();
    }

    @ExceptionHandler({ValidateException.class, DataTimeException.class, BookingException.class, EnumException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerBadRequest(final RuntimeException exception) {
        log.debug("Получен статус 400 Bad request {}", exception.getMessage(), exception);
        return ErrorResponse.builder().error(exception.getMessage()).build();
    }

    @Data
    @Builder
    public static class ErrorResponse {
        private final String error;
    }
}


