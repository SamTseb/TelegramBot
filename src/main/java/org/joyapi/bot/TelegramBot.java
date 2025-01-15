package org.joyapi.bot;

import org.joyapi.exception.TelegramSendMessageException;
import org.joyapi.model.enums.BotOption;
import org.joyapi.service.AuthorService;
import org.joyapi.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
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

    private final UserService userService;
    private final String botUsername;
    private final String botToken;

    /**
     * Constructs a new TelegramBot.
     *
     * @param userService the user service
     * @param authorService the author service
     * @param botToken the bot token
     * @param botUsername the bot username
     */
    public TelegramBot(UserService userService, AuthorService authorService,
                       @Value("${telegram-bot.token}") String botToken,
                       @Value("${telegram-bot.username}") String botUsername) {
        this.userService = userService;
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    /**
     * Returns the bot username.
     *
     * @return the bot username
     */
    @Override
    public String getBotUsername() {
        return botUsername;
    }

    /**
     * Returns the bot token.
     *
     * @return the bot token
     */
    @Override
    public String getBotToken() {
        return botToken;
    }

    /**
     * Handles incoming updates from Telegram.
     *
     * @param update the update received
     */
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

            userService.addPostToFavorites(Long.valueOf(callbackQuery.getData()), userId);
            sendTextMessageByUserId("It was added to favorites", userId);
        }
    }

    /**
     * Sends a text message to a user by their user ID.
     *
     * @param messageText the message text
     * @param userId the user ID
     */
    public void sendTextMessageByUserId(String messageText, Long userId) {
        Long chatId = userService.getUser(userId).getChatID();
        sendTextMessageByChatId(messageText, chatId);
    }

    /**
     * Sends a text message to a user by their chat ID.
     *
     * @param messageText the message text
     * @param chatId the chat ID
     */
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
                    Message:%s
                    Exception: %s""", chatId, message.getText(), e.getMessage()));
        }
    }

    /**
     * Sends an image to a user by their user ID.
     *
     * @param imageFile the image file
     * @param userId the user ID
     */
    public void sendImage(File imageFile, Long userId) throws TelegramApiException {
        Long chatId = userService.getUser(userId).getChatID();
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(imageFile));

        execute(sendPhoto);
    }

    public void sendVideo(File videoFile, Long userId) throws TelegramApiException {
        Long chatId = userService.getUser(userId).getChatID();
        SendVideo sendVideo = new SendVideo();
        sendVideo.setChatId(chatId);
        sendVideo.setVideo(new InputFile(videoFile));

        execute(sendVideo);
    }

    /**
     * Sends a reaction message to a user by their user ID.
     *
     * @param postId the post ID
     * @param userId the user ID
     */
    public void sendReactionMessage(Long postId, Long userId) {
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
                    Message:%s
                    Exception: %s""", chatId, message.getText(), e.getMessage()));
        }
    }

    /**
     * Creates reaction buttons for a post.
     *
     * @param postId the post ID
     * @return the inline keyboard markup with reaction buttons
     */
    private InlineKeyboardMarkup createReactionButtons(Long postId) {
        InlineKeyboardButton addToFavorite = InlineKeyboardButton.builder()
                .text("👍")
                .callbackData(postId.toString())
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(addToFavorite))
                .build();
    }

    /**
     * Chooses the bot option based on the message text.
     *
     * @param message the message text
     * @return the chosen bot option
     */
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

    /**
     * Extracts the main part of the message based on the bot option.
     *
     * @param inputMessage the input message
     * @param option the bot option
     * @return the main part of the message, or null if not found
     */
    private String getMessageMainPart(String inputMessage, BotOption option){
        String[] input = inputMessage.split(option.getValue() + " ");
        if (input.length == 2) {
            return input[1];
        } else {
            return null;
        }
    }
}

