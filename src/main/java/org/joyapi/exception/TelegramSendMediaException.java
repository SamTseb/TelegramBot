package org.joyapi.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TelegramSendMediaException extends RuntimeException{
    private String imageURL;
    private Long userId;

    public TelegramSendMediaException(String message, String imageURL, Long userId) {
        super(message);
        this.imageURL = imageURL;
        this.userId = userId;
    }
}
