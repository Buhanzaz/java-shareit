package shareit.gateway.exception.controller;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import shareit.gateway.exception.DataTimeException;
import shareit.gateway.exception.EnumException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerForViolationException(ConstraintViolationException exception) {
        return ErrorResponse.builder().error(exception.getMessage()).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerForViolationException(HttpStatusCodeException exception) {
        return ErrorResponse.builder().error(exception.getMessage()).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerForEnumException(final EnumException exception) {
        return ErrorResponse.builder().error(exception.getMessage()).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerForDataException(final DataTimeException exception) {
        return ErrorResponse.builder().error(exception.getMessage()).build();
    }

    @Data
    @Builder
    public static class ErrorResponse {
        private final String error;
    }
}


