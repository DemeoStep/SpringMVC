package com.oched.booksprj.controllers;

import com.oched.booksprj.exceptions.BadRequestException;
import com.oched.booksprj.responses.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.DateTimeException;
import java.util.Date;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(
            final HttpServletRequest request, final BadRequestException exception)
    {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                exception.getStatus().value(),
                exception.getMessage(),
                request.getServletPath()
        );

        log.error(exception.getMessage() + " at path '" + request.getServletPath() + "'");
        return new ResponseEntity<>(response, exception.getStatus());
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            DataIntegrityViolationException.class,
            DateTimeException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ExceptionResponse> handleInvalidRequests (
            final HttpServletRequest request, final Exception exception
    ) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                request.getServletPath()
        );

        log.error(exception.getMessage() + " at path '" + request.getServletPath() + "'");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException (
            final HttpServletRequest request, final Exception exception
    ) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                request.getServletPath()
        );

        log.error(exception.getMessage() + " at path '" + request.getServletPath() + "'");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
