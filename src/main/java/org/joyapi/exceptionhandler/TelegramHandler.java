package org.joyapi.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joyapi.bot.TelegramBot;
import org.joyapi.exception.TelegramSendMediaException;
import org.joyapi.exception.TelegramSendMessageException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@AllArgsConstructor
@ControllerAdvice
public class TelegramHandler {
    private final TelegramBot telegramBot;

    @ExceptionHandler(TelegramSendMessageException.class)
    public void handleAllExeptions(TelegramSendMessageException exception){
        log.warn(exception.getMessage());
    }

    @ExceptionHandler(TelegramSendMediaException.class)
    public void handleAllExeptions(TelegramSendMediaException exception){
        String message = exception.getMessage() + "\nURL: " + exception.getImageURL();
        log.warn(message);
        telegramBot.sendTextMessageByUserId(message, exception.getUserId());
    }
}
