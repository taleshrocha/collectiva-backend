package br.ufrn.imd.collectiva_backend.utils.exception;

import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException {

    private final HttpStatus httpStatusCode;

    /**
     * Constructs a new AuthException with the specified detail message and HTTP
     * status code.
     *
     * @param message    the detail message.
     * @param statusCode the HTTP status code associated with the error.
     */
    public AuthException(String message, HttpStatus statusCode) {
        super(message);
        this.httpStatusCode = statusCode;
    }

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }
}
