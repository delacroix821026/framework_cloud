package com.fw.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnregisteredUserException.class)
    public String handlerBaseRuntimeException(UnregisteredUserException ex) {
        return ex.getToken();
    }
}
