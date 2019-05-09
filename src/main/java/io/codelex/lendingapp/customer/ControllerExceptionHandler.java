package io.codelex.lendingapp.customer;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@ControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(InvalidStatusException.class)
    @ResponseStatus(BAD_REQUEST)
    public void handleCustomException() {
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(CONFLICT)
    public void handleInvalidRequestException() {
    }
}
