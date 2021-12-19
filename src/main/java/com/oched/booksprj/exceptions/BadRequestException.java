package com.oched.booksprj.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{
    private final HttpStatus status;

    public BadRequestException(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }

}
