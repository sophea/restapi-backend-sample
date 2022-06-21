package com.sma.backend.exception;

/**
 * encoding exception.
 *
 * @author sophea mak
 * @since 2022
 */
public class EncodingException extends RuntimeException {

    public EncodingException(String message) {
        super(message);
    }

    public EncodingException(String message, Throwable cause) {
        super(message, cause);
    }
}
