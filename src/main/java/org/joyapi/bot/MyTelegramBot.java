package org.joyapi.bot;

import lombok.AllArgsConstructor;
import org.joyapi.service.ImageDownloader;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
public class MyTelegramBot extends TelegramLongPollingBot {

    private final ImageDownloader imageDownloader;

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
        // Проверяем, есть ли сообщение и текст в обновлении
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();

            // Ответ на сообщение
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Вы сказали: " + messageText);
        }
    }
}

