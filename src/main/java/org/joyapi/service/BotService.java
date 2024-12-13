package org.joyapi.service;

import lombok.AllArgsConstructor;
import org.joyapi.bot.MyTelegramBot;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@AllArgsConstructor
@Service
public class BotService {
    private final ImageDownloader imageDownloader;

    public void botRun() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyTelegramBot(imageDownloader));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
