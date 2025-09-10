package com.shortlink.exception;

import org.springframework.http.HttpStatus;

public class ShortUrlException extends ApiException{

    public ShortUrlException(HttpStatus httpStatus,String message) {
        super("INVALID_URL", message, httpStatus);
    }
}
