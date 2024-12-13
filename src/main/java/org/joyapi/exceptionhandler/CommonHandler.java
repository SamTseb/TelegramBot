package org.joyapi.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.joyapi.exception.ImageDownloadException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class CommonHandler {

    @ExceptionHandler(ImageDownloadException.class)
    public void handleAllExeptions(Exception exception){
        log.warn(exception.getMessage());
    }
}
