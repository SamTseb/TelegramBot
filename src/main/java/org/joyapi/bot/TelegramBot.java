package org.joyapi.bot;

import org.joyapi.exception.TelegramSendImageException;
import org.joyapi.exception.TelegramSendMessageException;
import org.joyapi.service.ImageDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    public TelegramBot(ImageDownloadService imageDownloadService){
        this.imageDownloadService = imageDownloadService;
    }

    private final ImageDownloadService imageDownloadService;
    private static final String CHAT_ID = "843593235";

    @Override
    public String getBotUsername() {
        return "JoyDecBot"; // Замените на имя вашего бота
    }

    @Override
    public String getBotToken() {
        return "7810877451:AAGtnDkSku01n8OZHxm66bCJ9vFKwXWyxeY"; // Замените на ваш токен
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            File imageFile = imageDownloadService.downloadImage(messageText);

            sendTextMessage("Here your image");
            sendImage(imageFile);
        }
    }

    public void sendTextMessage(String messageText) {
        SendMessage message = new SendMessage();
        message.setText(messageText);
        message.setChatId(CHAT_ID);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramSendMessageException(String.format("""
                                                                Error occurred during sending message to user!
                                                                ChatID:%s
                                                                Message:%s""", CHAT_ID, messageText));
        }
    }

    public void sendImage(File imageFile){
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(CHAT_ID);
        sendPhoto.setPhoto(new InputFile(imageFile));

        try {
            execute(sendPhoto);
        } catch(TelegramApiException exception){
            throw new TelegramSendImageException(String.format("""
                                                                Error occurred during sending message to user!
                                                                ChatID:%s""", CHAT_ID));
        }
    }
}

