package info.navnoire.recipeappserver_springboot.controller.exhandling;

import info.navnoire.recipeappserver_springboot.controller.payload.response.ErrorMessageResponse;
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
    public ErrorMessageResponse resourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        return new ErrorMessageResponse(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessageResponse globalExceptionHandler(Exception exception, WebRequest request) {
        return new ErrorMessageResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                exception.getMessage());
    }
}
