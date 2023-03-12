package com.guavapay.parceldeliveryapp.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail runtimeIllegalArgument(IllegalArgumentException exception) {
        log.info(exception.getMessage());
        var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(exception.getMessage());
        return problemDetail;
    }

    @ExceptionHandler({
            BadRequestException.class,
            NoSuchFieldException.class,
            NumberFormatException.class,
            JsonProcessingException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail runtime(RuntimeException exception) {
        log.info(exception.getMessage());
        var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(exception.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail notFoundHandler(NotFoundException exception) {
        log.info(exception.getMessage());
        var problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setDetail(exception.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail httpClientErrorHandler(HttpClientErrorException exception) {
        log.info(exception.getMessage());

        var problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setDetail(exception.getMessage());
        return problemDetail;
    }
}
