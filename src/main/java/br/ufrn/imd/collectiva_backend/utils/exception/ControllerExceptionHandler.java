package br.ufrn.imd.collectiva_backend.utils.exception;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.ufrn.imd.collectiva_backend.dto.ApiResponseDTO;
import br.ufrn.imd.collectiva_backend.dto.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * Global exception handler for controllers in the application.
 * Handles various types of exceptions and maps them to appropriate error
 * responses.
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * Handles BusinessException and maps it to a custom error response.
     *
     * @param exception The BusinessException instance.
     * @param request   The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> businessException(BusinessException exception,
            HttpServletRequest request) {

        logger.error(exception.getMessage(), exception);

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                exception.getHttpStatusCode().value(),
                "Business error",
                exception.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(exception.getHttpStatusCode()).body(new ApiResponseDTO<>(
                false,
                "Error: " + exception.getMessage(),
                null,
                err));
    }

    /**
     * Handles ResourceNotFoundException and maps it to a custom error response.
     *
     * @param exception The ResourceNotFoundException instance.
     * @param request   The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> notFound(ResourceNotFoundException exception,
            HttpServletRequest request) {

        logger.error(exception.getMessage(), exception);

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Resource not found",
                exception.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO<>(
                false,
                "Error: " + exception.getMessage(),
                null,
                err));
    }

    /**
     * Handles ConversionException and maps it to a custom error response.
     *
     * @param exception The ConversionException instance.
     * @param request   The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(ConversionException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> conversionException(ConversionException exception,
            HttpServletRequest request) {

        logger.error(exception.getMessage(), exception);

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected problem occurred while converting data.",
                "",
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDTO<>(
                false,
                "",
                null,
                err));

    }

    /**
     * Handles TransactionSystemException and maps it to a custom error response.
     *
     * @param exception The TransactionSystemException instance.
     * @param request   The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler({ TransactionSystemException.class })
    protected ResponseEntity<ApiResponseDTO<ErrorDTO>> handlePersistenceException(Exception exception,
            HttpServletRequest request) {
        logger.error(exception.getMessage(), exception);

        Throwable cause = ((TransactionSystemException) exception).getRootCause();
        if (cause instanceof ConstraintViolationException consEx) {
            final List<String> errors = new ArrayList<>();
            for (final ConstraintViolation<?> violation : consEx.getConstraintViolations()) {
                errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
            }

            final var err = new ErrorDTO(
                    ZonedDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Transaction Error",
                    "",
                    request.getRequestURI());

            logger.error(errors.toString());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDTO<>(
                    false,
                    "Error",
                    null,
                    err));
        }
        return internalErrorException(exception, request);
    }

    /**
     * Handles FeignClientException and maps it to a custom error response.
     *
     * @param e       The FeignClientException instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> authException(AuthException e,
            HttpServletRequest request) {
        logger.error(e.getMessage(), e);

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                e.getHttpStatusCode().value(),
                "Auth error",
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(e.getHttpStatusCode()).body(new ApiResponseDTO<>(
                false,
                "",
                null,
                err));
    }

    /**
     * Handles AccessDeniedException and maps it to a custom error response.
     *
     * @param e       The AccessDeniedException instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> accessDeniedException(AccessDeniedException e,
            HttpServletRequest request) {

        logger.error(e.getMessage(), e);

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Access denied",
                "",
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponseDTO<>(
                false,
                "Error: " + e.getMessage(),
                null,
                err));
    }

    /**
     * Handles any other unexpected exception and maps it to a generic error
     * response.
     *
     * @param e       The unexpected Exception instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> internalErrorException(Exception e,
            HttpServletRequest request) {

        logger.error(e.getMessage(), e);

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected problem occurred.",
                "",
                request.getRequestURI());

        logger.error("An unexpected problem occurred. ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDTO<>(
                false,
                "Error",
                null,
                err));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> handleMissingServletRequestPartException(
            MissingServletRequestPartException exception, HttpServletRequest request) {

        logger.error("Missing part in request: {}", exception.getMessage(), exception);

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Missing part in request",
                "Required part(s) of the request are missing.",
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDTO<>(
                false,
                "Required part(s) of the request are missing.",
                null,
                err));
    }

}