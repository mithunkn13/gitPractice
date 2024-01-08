package com.blog.exception;

import org.springframework.http.HttpStatus;

public class BlogAPIException extends RuntimeException {
    public BlogAPIException(HttpStatus httpStatus, String invalidJwtSignature) {
    }
}
