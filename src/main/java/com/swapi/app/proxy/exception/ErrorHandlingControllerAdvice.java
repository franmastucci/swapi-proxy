package com.swapi.app.proxy.exception;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
class ErrorHandlingControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    ErrorResponse onNotFoundException(NotFoundException e){
        return  ErrorResponse.builder()
                .msg(e.getMessage())
                .code(HttpStatus.NOT_FOUND.value())
                .build();
    }

    @ExceptionHandler(InternalProxyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    ErrorResponse onInternalxception(InternalProxyException e){
        return  ErrorResponse.builder()
                .msg(e.getMessage())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }

}
