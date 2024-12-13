package org.joyapi.exception;

public class TelegramSendImageException extends RuntimeException{
    public TelegramSendImageException(String message){
        super(message);
    }
}
