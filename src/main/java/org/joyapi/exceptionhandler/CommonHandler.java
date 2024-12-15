package org.joyapi.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.joyapi.exception.AuthorNotFoundException;
import org.joyapi.exception.ImageDownloadException;
import org.joyapi.exception.UserNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CommonHandler {

    @ExceptionHandler({ImageDownloadException.class, AuthorNotFoundException.class, UserNotFoundException.class})
    public void handleAllExeptions(Exception exception){
        log.warn(exception.getMessage());
    }
}
