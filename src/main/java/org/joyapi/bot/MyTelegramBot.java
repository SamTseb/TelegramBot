package org.joyapi.bot;

import lombok.AllArgsConstructor;
import org.joyapi.exception.TelegramSendImageException;
import org.joyapi.exception.TelegramSendMessageException;
import org.joyapi.service.ImageDownloadService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
public class MyTelegramBot extends TelegramLongPollingBot {

    private final ImageDownloadService imageDownloadService;
    private String chatId;

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
            chatId = update.getMessage().getChatId().toString();

            File imageFile = imageDownloadService.downloadImage(messageText);

            sendTextMessage("Here your image");
            sendImage(imageFile);
        }
    }

    private void sendTextMessage(String messageText) {
        SendMessage message = new SendMessage();
        message.setText(messageText);
        message.setChatId(chatId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramSendMessageException(String.format("""
                                                                Error occurred during sending message to user!
                                                                ChatID:%s
                                                                Message:%s""", chatId, messageText));
        }
    }

    private void sendImage(File imageFile){
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(imageFile));

        try {
            execute(sendPhoto);
        } catch(TelegramApiException exception){
            throw new TelegramSendImageException(String.format("""
                                                                Error occurred during sending message to user!
                                                                ChatID:%s""", chatId));
        }
    }
}

