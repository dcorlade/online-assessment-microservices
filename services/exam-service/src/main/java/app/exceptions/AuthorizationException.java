package app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthorizationException {
    @ExceptionHandler(value = SecurityException.class)
    public ResponseEntity<String> exception() {
        return new ResponseEntity<>("User is not authorized", HttpStatus.FORBIDDEN);
    }
}
