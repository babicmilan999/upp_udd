package upp_udd.project.exception;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        String message = ex.getMessage();
        log.info("{}: {}", ex.getClass().getSimpleName(), message);
        return buildErrorResponseEntity(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(EntityNotFoundException ex) {
        String message = ex.getMessage();
        log.info("{}: {}", ex.getClass().getSimpleName(), message);
        return buildErrorResponseEntity(HttpStatus.NOT_FOUND, message);
    }

    private ResponseEntity<Object> buildErrorResponseEntity(HttpStatus httpStatus, String message) {
        return new ResponseEntity(ErrorResponseDto.builder().status(httpStatus.value()).message(message).build(), httpStatus);
    }

}
