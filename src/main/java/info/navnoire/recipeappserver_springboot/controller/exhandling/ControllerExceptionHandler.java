package info.navnoire.recipeappserver_springboot.controller.exhandling;

import info.navnoire.recipeappserver_springboot.controller.payload.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public MessageResponse resourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        return new MessageResponse(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                exception.getMessage());
    }

    @ExceptionHandler(TokenRefreshException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public MessageResponse tokenRefreshException(TokenRefreshException exception, WebRequest request) {
        return new MessageResponse(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                exception.getMessage());
    }

    @ExceptionHandler(DuplicateUserDataException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public MessageResponse duplicateDataException(DuplicateUserDataException exception, WebRequest request) {
        return new MessageResponse(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                exception.getMessage());
    }
}
