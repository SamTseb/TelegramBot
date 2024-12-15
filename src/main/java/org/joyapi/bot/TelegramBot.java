package org.joyapi.bot;

import lombok.AllArgsConstructor;
import org.joyapi.exception.TelegramSendImageException;
import org.joyapi.exception.TelegramSendMessageException;
import org.joyapi.model.enums.BotOption;
import org.joyapi.service.AuthorService;
import org.joyapi.service.PostService;
import org.joyapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final AuthorService authorService;
    private final UserService userService;
    private final String botUsername;
    private final String botToken;

    public TelegramBot(UserService userService, AuthorService authorService,
                       @Value("${telegram-bot.token}") String botToken,
                       @Value("${telegram-bot.username}") String botUsername) {
        this.authorService = authorService;
        this.userService = userService;
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            Long userId = update.getMessage().getFrom().getId();
            switch (chooseOption(update.getMessage().getText())){
                case start:
                    String cookies = getMessageMainPart(update.getMessage().getText(), BotOption.start);
                    if (cookies != null) {
                        userService.addNewUser(update.getMessage(), cookies);

                        sendTextMessageByUserId("""
                                                                Warm welcome!
                                                                
                                                                To add your new favorite artist put his name after /new_author
                                                                To sync your posts - /sync_posts
                                                                """, userId);
                    } else {
                        sendTextMessageByChatId("Put your cookies after " + BotOption.start.getValue(),
                                                update.getMessage().getChatId());
                    }
                    break;
                case new_author:
                    String authorName = getMessageMainPart(update.getMessage().getText(), BotOption.new_author);
                    if (authorName != null) {
                        userService.newAuthor(authorName, userId);
                        userService.syncPost(userId);

                        sendTextMessageByUserId("Author was saved: " + authorName, userId);
                    } else {
                        sendTextMessageByChatId("Put author name after " + BotOption.new_author.getValue(),
                                update.getMessage().getChatId());
                    }
                    break;
                case sync_posts:
                    userService.syncPost(userId);

                    sendTextMessageByUserId("Your posts were synced", userId);
                    break;
                case invalid_option:
                    sendTextMessageByUserId("You chose an invalid option", userId);
            }
        }
        if (update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Long userId = callbackQuery.getFrom().getId();

            userService.addPostToFavorites(callbackQuery.getData(), userId);
            sendTextMessageByUserId("It was added to favorites", userId);
        }
    }

    public void sendTextMessageByUserId(String messageText, Long userId) {
        Long chatId = userService.getUser(userId).getChatID();
        sendTextMessageByChatId(messageText, chatId);
    }

    public void sendTextMessageByChatId(String messageText, Long chatId) {
        SendMessage message = new SendMessage();
        message.setText(messageText);
        message.setChatId(chatId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramSendMessageException(String.format("""
                    Error occurred during sending message to user!
                    ChatID:%s
                    Message:%s""", chatId, message.getText()));
        }
    }

    public void sendImage(File imageFile, Long userId) {
        Long chatId = userService.getUser(userId).getChatID();
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(imageFile));
        try {
            execute(sendPhoto);
        } catch (TelegramApiException exception) {
            throw new TelegramSendImageException(String.format("""
                    Error occurred during sending message to user!
                    ChatID:%s""", chatId));
        }
    }

    public void sendImageList(List<File> imageList, Long userId) {
        imageList.forEach(image -> sendImage(image, userId));
    }

    public void sendReactionMessage(String postId, Long userId) {
        Long chatId = userService.getUser(userId).getChatID();
        SendMessage message = SendMessage.builder()
                                        .chatId(chatId)
                                        .text("To favorites?")
                                        .replyMarkup(createReactionButtons(postId))
                                        .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramSendMessageException(String.format("""
                    Error occurred during sending reaction message to user!
                    ChatID:%s
                    Message:%s""", chatId, message.getText()));
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

    private BotOption chooseOption(String message){
        if (message.contains("/start")){
            return BotOption.start;
        } else if (message.contains("/new_author")) {
            return BotOption.new_author;
        } else if (message.contains("/sync_post")) {
            return BotOption.sync_posts;
        }
        return BotOption.invalid_option;
    }

    private String getMessageMainPart(String inputMessage, BotOption option){
        String[] input = inputMessage.split(option.getValue() + " ");
        if (input.length == 2) {
            return input[1];
        } else {
            return null;
        }
    }
}

