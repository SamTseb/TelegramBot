package org.joyapi.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.joyapi.exception.TelegramSendImageException;
import org.joyapi.exception.TelegramSendMessageException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@ControllerAdvice
public class TelegramHandler {

    @ExceptionHandler({TelegramSendMessageException.class, TelegramSendImageException.class})
    public void handleAllExeptions(TelegramApiException exception){
        log.warn(exception.getMessage());
    }
}
