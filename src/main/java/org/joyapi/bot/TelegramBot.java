package org.joyapi.bot;

import org.joyapi.exception.TelegramSendImageException;
import org.joyapi.exception.TelegramSendMessageException;
import org.joyapi.service.AuthorService;
import org.joyapi.service.ImageDownloadService;
import org.joyapi.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final AuthorService authorService;
    private final PostService postService;
    private static final String CHAT_ID = "843593235";

    @Autowired
    public TelegramBot(AuthorService authorService, PostService postService) {
        this.authorService = authorService;
        this.postService = postService;
    }

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

        /*if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().contains("/new_author")) {
                String authorName = update.getMessage().getText().split(" ")[1];
                authorService.newAuthor(authorName);

                sendTextMessage("Author was saved: " + authorName);
            }
        }*/
        if (update.hasMessage() && update.getMessage().hasText()) {
            String authorName = update.getMessage().getText();
            authorService.newAuthor(authorName);

            sendTextMessage("Author was saved: " + authorName);
        }
        if (update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            /// TODO Find a way to add to favorite
            postService.addPostToFavorites(callbackQuery.getData());
            sendTextMessage("It was added to favorites");
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

    public void sendImage(File imageFile) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(CHAT_ID);
        sendPhoto.setPhoto(new InputFile(imageFile));
        try {
            execute(sendPhoto);
        } catch (TelegramApiException exception) {
            throw new TelegramSendImageException(String.format("""
                    Error occurred during sending message to user!
                    ChatID:%s""", CHAT_ID));
        }
    }

    public void sendImageList(List<File> imageList) {
        imageList.forEach(this::sendImage);
    }

    public void sendReactionMessage(String postId) {
        SendMessage message = SendMessage.builder()
                                        .chatId(CHAT_ID)
                        //                .text("Пожалуйста, выберите свою реакцию:")
                                        .replyMarkup(createReactionButtons(postId))
                                        .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup createReactionButtons(String postId) {
        InlineKeyboardButton addToFavorite = InlineKeyboardButton.builder()
                .text("👍")
                .callbackData(postId)
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(addToFavorite))
                .build();
    }
}

