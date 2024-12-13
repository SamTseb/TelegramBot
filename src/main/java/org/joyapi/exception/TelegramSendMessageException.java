package org.joyapi.exception;

public class TelegramSendMessageException extends RuntimeException{
    public TelegramSendMessageException(String message){
        super(message);
    }
}
