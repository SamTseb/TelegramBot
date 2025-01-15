package org.joyapi.service;

import lombok.extern.slf4j.Slf4j;
import org.joyapi.bot.TelegramBot;
import org.joyapi.exception.TelegramSendMediaException;
import org.joyapi.model.Author;
import org.joyapi.model.Post;
import org.joyapi.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Service class for updating post to users.
 */
@Slf4j
@Service
public class SubscribeService {
    private final TelegramBot telegramBot;
    private final MediaDownloadService mediaDownloadService;
    private final PostService postService;
    private final UserService userService;

    private static final int PAGE_NUMBER = 0;
    private final Integer postAmount;

    public SubscribeService(TelegramBot telegramBot,
                            MediaDownloadService mediaDownloadService,
                            PostService postService,
                            UserService userService,
                            @Value("${subscription.max-amount}") Integer postAmount) {
        this.telegramBot = telegramBot;
        this.mediaDownloadService = mediaDownloadService;
        this.postService = postService;
        this.userService = userService;
        this.postAmount = postAmount;
    }

    /**
     * Subscribes users to new posts from their favorite authors.
     * It is scheduled to run every hour.
     */
//    @Transactional
//    @Scheduled(cron = "0 0 * * * *")
//    @Scheduled(cron = "0 20 * * * *")
//    @Scheduled(cron = "0 28/15 * * * *")
    @Scheduled(cron = "0 * * * * *")
    public void subscribe() {
        List<User> users = userService.getUsers();
        for (User user : users) {
            log.info("New post for a user with ID {}", user.getId());
            Set<Author> allAuthors = user.getFavoriteAuthors();
            for (Author author : allAuthors) {
                String prohibitedTags = user.getProhibitedTags() != null ?
                                        user.getProhibitedTags() : "";
                List<Post> posts = postService.getAndSavePosts(postAmount, PAGE_NUMBER, author.getName() + prohibitedTags);

                if(posts.isEmpty()) {
                    continue;
                }
                log.info("New post of {}", author.getName());
                telegramBot.sendTextMessageByUserId("New post of " + author.getName(), user.getId());
                postService.logNewPosts(posts);


                for (Post post : posts) {
                    String mediaUrl = post.getFileUrl();
                    File media = mediaDownloadService.downloadMedia(mediaUrl);
                    try {
                        sendMedia(media, user);
                    } catch (TelegramSendMediaException exception) {
                        exception.setImageURL(mediaUrl);
                        exception.setUserId(user.getId());
                        throw exception;
                    }
                    telegramBot.sendReactionMessage(post.getPostId(), user.getChatID());
                }
                log.info("End of new post of {}", author.getName());
            }
        }
    }

    private void sendMedia(File image, User user) {
        if (mediaDownloadService.getMediaType(image.getName()).equals("mp4")) {
            try {
                telegramBot.sendVideo(image, user.getId());
            } catch (TelegramApiException e) {
                throw new TelegramSendMediaException(String.format("""
                                                                    Error occurred during sending video to user!
                                                                    UserID:%s
                                                                    Exception: %s""", user.getId(), e.getMessage()),
                                                    null,
                                                    null);
            }
        } else {
            try {
                telegramBot.sendImage(image, user.getId());
            } catch (TelegramApiException e) {
                throw new TelegramSendMediaException(String.format("""
                                                    Error occurred during sending image to user!
                                                    UserID:%s
                                                    Exception: %s""", user.getId(), e.getMessage()),
                                                    null,
                                                    null);
            }
        }
    }
}