package com.oched.booksprj.responses;

import lombok.Value;

import java.util.Date;

@Value
public class ExceptionResponse {
    Date timestamp;
    int status;
    String error;
    String path;
}
