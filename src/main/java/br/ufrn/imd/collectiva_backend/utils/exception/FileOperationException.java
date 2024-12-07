package br.ufrn.imd.collectiva_backend.utils.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom exception class for handling file operation errors.
 * <p>
 * This exception is thrown when an error occurs during file operations.
 */
public class FileOperationException extends RuntimeException {

    private final HttpStatus httpStatusCode;

    public FileOperationException(String message, HttpStatus httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }
}
