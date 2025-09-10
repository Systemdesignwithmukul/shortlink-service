package com.shortlink.exception;

import org.springframework.http.HttpStatus;

public class HashGenerationApiException extends ApiException{
    public HashGenerationApiException(String message, HttpStatus httpStatus) {
        super("Hash-Collision", message, httpStatus);
    }
}
